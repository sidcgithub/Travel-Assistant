package com.siddharthchordia.travelassistant.ui.trips

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.FragmentTripsBinding
import com.siddharthchordia.travelassistant.model.City
import com.siddharthchordia.travelassistant.ui.trips.dialog.CityInputDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class TripsFragment : Fragment(), CityInputDialogFragment.CityInputListener {


    private val viewModel: TripsViewModel by viewModels()

    private lateinit var binding: FragmentTripsBinding
    private val adapter = TripAdapter()

    private var deviceLocationEnabled = false
    companion object {
        const val ACTION_GEOFENCE_EVENT = "action-geofence-event"
        const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 200
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 201

        private const val FINE_LOCATION_PERMISSION_INDEX = 0
        private const val COARSE_LOCATION_PERMISSION_INDEX = 1
        private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 2
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 1234
        private const val MY_PERMISSIONS_REQUEST_POST_NOTIFICATION = 900
    }

    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q



    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        return foregroundFineLocationApproved() && foregroundCoarseLocationApproved() && backgroundPermissionApproved()
    }
    fun foregroundFineLocationApproved() :Boolean = (
            PackageManager.PERMISSION_GRANTED ==
                    ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION))
    fun foregroundCoarseLocationApproved() :Boolean = (
            PackageManager.PERMISSION_GRANTED ==
                    ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION))

    fun  backgroundPermissionApproved() : Boolean =
        if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED ==
                    ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
        } else {
            true
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (
            grantResults.isEmpty() ||
            grantResults[FINE_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            grantResults[COARSE_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED))
        {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()

        }
    }

    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>

    private fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this.requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                resultLauncher.launch(intentSenderRequest)
            } else {
                deviceLocationEnabled = false
                Toast.makeText(requireContext(), "Location Setting should be enabled", Toast.LENGTH_SHORT).show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                deviceLocationEnabled = true
            }
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestBackgroundPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { permissions ->
        if ((permissions == true || !runningQOrLater)
        ) {
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return

        val permissionsArray = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        requestPermissionLauncher.launch(permissionsArray.toTypedArray())

        if (runningQOrLater) {
            permissionsArray.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            requestBackgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }


    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.tripList.adapter = adapter

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                deviceLocationEnabled = true
            } else {
                deviceLocationEnabled = false
                Toast.makeText(requireContext(), "Location Setting should be enabled", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tripList.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
    private fun removeTrip(id:Long, name:String) {
        Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()
        viewModel.removeCity(id, name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDeviceLocationSettingsAndStartGeofence()


        viewModel.cities.observe(viewLifecycleOwner) { cities ->
            val tripItems = cities.map { city ->
                TripItem(
                    id = city.id,
                    cityName = city.name,
                    startDate = city.startDate,
                    endDate = city.endDate,
                    geofenceId = "",
                    onRemoveTrip = { removeTrip(city.id.toLong(), city.name) }
                )

            }
            adapter.submitList(tripItems)
        }


        binding.fabAddTrip.setOnClickListener {
            val cityInputDialog = CityInputDialogFragment()
            cityInputDialog.cityInputListener = this
            cityInputDialog.show(childFragmentManager, "CityInputDialog")
        }

    }

    private fun isNotificationPermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(requireActivity(), POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                listOf(Manifest.permission.POST_NOTIFICATIONS).toTypedArray(),
                MY_PERMISSIONS_REQUEST_POST_NOTIFICATION);
            return false
        }
        return true
    }

    override fun onCityInput(cityName: String, startDate: Date, endDate: Date): Boolean {
        checkDeviceLocationSettingsAndStartGeofence()
        requestForegroundAndBackgroundLocationPermissions()

        return if(deviceLocationEnabled && foregroundAndBackgroundLocationPermissionApproved() && isNotificationPermissionGranted()) {
            viewModel.fetchCityCoordinates(cityName, startDate, endDate)
            true
        }
        else {

            Toast.makeText(requireContext(), "Location Setting should be enabled", Toast.LENGTH_SHORT)

            false
        }
    }
}
