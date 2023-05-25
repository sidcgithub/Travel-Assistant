package com.siddharthchordia.travelassistant.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.siddharthchordia.travelassistant.database.AttractionDao
import com.siddharthchordia.travelassistant.database.TravelDatabase
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.model.AttractionDetails
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.network.OpenTripMapApi
import javax.inject.Inject

class AttractionRepository @Inject constructor(private val service: OpenTripMapApi,
                                               private val travelDatabase: TravelDatabase
                                               ) {
    val attractions: MutableLiveData<List<Attraction>> = MutableLiveData()

    suspend fun getAttractionsInRadius(long: Double, lat: Double){
        attractions.value =   service.getAttractionsInRadius(radius = 10000, longitude = long, latitude = lat)
    }

    suspend fun insertAttractionIntoItinerary(attractionEntity: AttractionEntity) = travelDatabase.attractionDao().insert(attractionEntity)

    fun fetchAttractionsNotDone() = travelDatabase.attractionDao().getAttractionsNotDone()
    suspend fun update(attractionEntity: AttractionEntity) = travelDatabase.attractionDao().update(attractionEntity)
    suspend fun delete(attractionEntity: AttractionEntity) = travelDatabase.attractionDao().delete(attractionEntity)
    fun getCompletedAttractions(): LiveData<List<AttractionEntity>> = travelDatabase.attractionDao().getAttractionsDone()
}