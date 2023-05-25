package com.siddharthchordia.travelassistant.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.google.android.gms.location.LocationServices
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        var currentLat = 0.0
        var currentLong = 0.0


        viewModel.cities.observe(viewLifecycleOwner) { it ->
            if(it.isNullOrEmpty().not()) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    Log.d("Fused", "onViewCreated: Working")
                    location?.let {
                        currentLat = location.latitude
                        currentLong = location.longitude
                        viewModel.updateCurrentCity(location.latitude, location.longitude)
                    }
                }
            }
        }

        viewModel.currentCity.observe(viewLifecycleOwner) { city ->
            city?.let {

                binding.cityName.text = city.name
                binding.exploreButton.isEnabled= true
            } ?: run {

                binding.cityName.text = "No active trip detected"
                binding.exploreButton.isEnabled= false
            }
        }

        binding.exploreButton.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToAttractionsFragment(
                currentLat.toFloat(),
                currentLong.toFloat()
            )
            findNavController(this).navigate(action)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}