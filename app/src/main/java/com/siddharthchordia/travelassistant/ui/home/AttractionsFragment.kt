package com.siddharthchordia.travelassistant.ui.home

import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.FragmentAttractionsBinding
import com.siddharthchordia.travelassistant.databinding.FragmentHomeBinding
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.ui.home.adapter.AttractionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttractionsFragment : Fragment() {
    private var _binding: FragmentAttractionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AttractionsViewModel by viewModels()

    private lateinit var attractionsAdapter: AttractionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttractionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    val args: AttractionsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val latitude = args.latitude
        val longitude = args.longitude
        viewModel.fetchAttractions(lat = latitude.toDouble(), long =  longitude.toDouble())

        attractionsAdapter = AttractionsAdapter { attraction ->
            val action = AttractionsFragmentDirections
                .actionAttractionsFragmentToAttractionDetailsFragment2(attraction.xid)
            findNavController(this).navigate(action)
        }

        binding.attractionRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = attractionsAdapter
        }



        viewModel.attractions.observe(viewLifecycleOwner) { attractions ->
            attractions.filter { it.name?.isNotBlank() == true }.let { attractionList ->
                val validAttractions = attractionList.sortedBy { it.name }
                attractionsAdapter.submitList(validAttractions)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
