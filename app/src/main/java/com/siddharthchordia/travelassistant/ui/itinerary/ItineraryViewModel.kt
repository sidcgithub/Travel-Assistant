package com.siddharthchordia.travelassistant.ui.itinerary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val attractionRepository: AttractionRepository
) : ViewModel() {

    val attractions: LiveData<List<AttractionEntity>> = attractionRepository.fetchAttractionsNotDone()

    fun markAsDone(attraction: AttractionEntity) {
        viewModelScope.launch {
            attraction.done = true
            attractionRepository.update(attraction)
        }
    }

    fun removeAttraction(attraction: AttractionEntity) {
        viewModelScope.launch {
            attractionRepository.delete(attraction)
        }
    }
}