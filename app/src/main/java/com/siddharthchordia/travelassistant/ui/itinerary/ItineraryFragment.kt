package com.siddharthchordia.travelassistant.ui.itinerary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.siddharthchordia.travelassistant.R
import com.siddharthchordia.travelassistant.databinding.FragmentItineraryBinding
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.ui.itinerary.adapter.AttractionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItineraryFragment : Fragment() {
    private val viewModel: ItineraryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItineraryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = AttractionsAdapter(object : AttractionsAdapter.AttractionListener {
            override fun onDoneClicked(attraction: AttractionEntity) {
                viewModel.markAsDone(attraction)
            }

            override fun onRemoveClicked(attraction: AttractionEntity) {
                viewModel.removeAttraction(attraction)
            }
        })
        binding.attractionsRecyclerView.adapter = adapter

        viewModel.attractions.observe(viewLifecycleOwner) { attractions ->
            adapter.submitList(attractions)
        }

        binding.viewTravelHistoryButton.setOnClickListener {
            findNavController(this).navigate(R.id.action_navigation_notifications_to_historyFragment)
        }

        return binding.root
    }
}
