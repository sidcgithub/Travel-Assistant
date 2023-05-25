package com.siddharthchordia.travelassistant.ui.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddharthchordia.travelassistant.model.City
import com.siddharthchordia.travelassistant.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {


    val cities: LiveData<List<City>> = cityRepository.fetchTripList()

    val currentCity: MutableLiveData<City?> = MutableLiveData(null)

    val upcomingCity: LiveData<City?> = cityRepository.fetchNextUpcomingCity()

    fun updateCurrentCity(latitude: Double, longitude: Double) {
        val currentLocation = Location("currentLocation").apply {
            this.latitude = latitude
            this.longitude = longitude
        }
        viewModelScope.launch {
            currentCity.value = cities.value?.firstOrNull { city ->

                val cityLocation = Location("cityLocation").apply {
                    this.latitude = city.lat
                    this.longitude = city.lon
                }
                Log.d("Distance", "updateCurrentCity: ${cityLocation.distanceTo(currentLocation)}")

                (cityLocation.distanceTo(currentLocation) <= 10000 && isDateInRange(
                    city.startDate, city.endDate
                ))
            }
        }

    }

    private fun isDateInRange(startDate: Date, endDate: Date): Boolean {
        val currentDate = Date()
        return currentDate.after(startDate) && currentDate.before(endDate)
    }
}