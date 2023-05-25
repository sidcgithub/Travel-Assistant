package com.siddharthchordia.travelassistant.ui.home

import android.telephony.TelephonyManager.NetworkSlicingException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siddharthchordia.travelassistant.database.TravelDatabase
import com.siddharthchordia.travelassistant.di.NetworkModule
import com.siddharthchordia.travelassistant.model.AttractionDetails
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.repository.AttractionDetailsRepository
import com.siddharthchordia.travelassistant.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AttractionDetailsViewModel @Inject constructor(
    private val attractionDetailsRepository: AttractionDetailsRepository,
    private val attractionRepository: AttractionRepository
) : ViewModel() {
    private var _attractionDetails = MutableLiveData<AttractionDetails>()
    val attractionsAdded = attractionRepository.fetchAttractionsNotDone()
    val attractionDetails: LiveData<AttractionDetails> = _attractionDetails
    val saveButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(false)


    fun fetchAttractionDetails(xid: String) {
        viewModelScope.launch {
            _attractionDetails.value = try {
                attractionDetailsRepository.getAttractionDetails(xid)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        }

    }

    fun enableSaveButton(xid: String, attractionsList: List<AttractionEntity>): Boolean   {
            val temp = attractionsList.firstOrNull { it.xid == xid }
            return temp == null


    }

    fun saveAttraction() {
        val currentAttraction = _attractionDetails.value
        currentAttraction?.let {
            val attractionEntity = AttractionEntity(
                name = it.name,
                xid = it.xid ?: "",
            )
            viewModelScope.launch {
                attractionRepository.insertAttractionIntoItinerary(attractionEntity)
            }
        }
    }
}