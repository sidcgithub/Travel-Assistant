package com.siddharthchordia.travelassistant.ui.trips

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.siddharthchordia.travelassistant.GeofenceBroadcastReceiver
import com.siddharthchordia.travelassistant.model.City
import com.siddharthchordia.travelassistant.repository.CityRepository
import com.siddharthchordia.travelassistant.ui.trips.TripsFragment.Companion.ACTION_GEOFENCE_EVENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val cityRepository: CityRepository,
    private val application: Application
) : ViewModel() {

    val cities: LiveData<List<City>> = cityRepository.fetchTripList()
    private val geofencingClient = LocationServices.getGeofencingClient(application)
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(application, GeofenceBroadcastReceiver::class.java).apply {
            action = ACTION_GEOFENCE_EVENT
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE )
        } else {
            PendingIntent.getBroadcast(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }


    private fun addCity(city: City): Boolean {
        val duplicateCity = cities.value?.firstOrNull {
            it.name == city.name
        }

        if (duplicateCity != null) {
            return false
        } else {
            val geofence = Geofence.Builder()
                .setRequestId(city.name) // Geofence ID
                .setCircularRegion(
                    city.lat,
                    city.lon,
                    20000f
                ) // Geofence region (1000 meters radius)
                .setExpirationDuration(city.endDate.time - System.currentTimeMillis()) // Geofence duration
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)// Monitor enters and exits
                .build()

            val geofencingRequest = GeofencingRequest.Builder()
                .addGeofence(geofence)
                .build()

            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
            viewModelScope.launch {
                cityRepository.saveCityWithGeofenceId(city)

                val workRequest = OneTimeWorkRequest.Builder(RemoveGeofenceWorker::class.java)
                    .setInitialDelay(city.endDate.time, TimeUnit.MILLISECONDS) // set the delay until the end date
                    .setInputData(workDataOf("geofenceId" to city.name)) // pass the geofence ID to the worker
                    .build()

                WorkManager.getInstance(application).enqueue(workRequest)
            }
            return true
        }
    }

    fun removeCity(cityId: Long, city: String) {

        geofencingClient.removeGeofences(listOf(city))
            .addOnSuccessListener {
                viewModelScope.launch {
                    cityRepository.removeCityAndGeofence(cityId)
                }
            }
            .addOnFailureListener {
            }


    }

    fun fetchCityCoordinates(city: String, startDate: Date, endDate: Date) {
        if (startDate.before(Date(System.currentTimeMillis() - 3600000))) {
            Toast.makeText(application, "Invalid Date", Toast.LENGTH_SHORT).show()
            return
        }

        if (endDate.before(startDate)) {
            Toast.makeText(application, "Invalid Date Order", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch {
            val cityGeoData = cityRepository.fetchCityLatLong(city)
            cityGeoData?.let {
                addCity(
                    City(
                        name = it.name,
                        lat = it.lat.toDouble(),
                        lon = it.lon.toDouble(),
                        startDate = startDate,
                        endDate = endDate
                    )
                )
                cityRepository.fetchTripList()
            }
        }
    }
}

class RemoveGeofenceWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val geofencingClient = LocationServices.getGeofencingClient(appContext)


    override fun doWork(): Result {
        val geofenceId = inputData.getString("geofenceId")
        if (geofenceId.isNullOrBlank().not()) {
            geofencingClient.removeGeofences(listOf(geofenceId))
                .addOnSuccessListener {

                }
                .addOnFailureListener {
                }
        }
        return Result.success()
    }
}

