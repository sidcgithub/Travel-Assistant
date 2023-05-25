package com.siddharthchordia.travelassistant.ui.itinerary

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val attractionRepository: AttractionRepository
) : ViewModel() {

    val completedAttractions: LiveData<List<AttractionEntity>> = attractionRepository.getCompletedAttractions()
}