package com.siddharthchordia.travelassistant.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AttractionsViewModel @Inject constructor(
    private val attractionsRepository: AttractionRepository
) : ViewModel() {

    private val _attractions = attractionsRepository.attractions
    val attractions: LiveData<List<Attraction>> get() = _attractions

    fun fetchAttractions(lat: Double, long: Double) {

        viewModelScope.launch {

            attractionsRepository.getAttractionsInRadius(
                lat = lat,
                long = long
            )

        }
    }

}
