package com.siddharthchordia.travelassistant.repository

import com.siddharthchordia.travelassistant.database.TravelDatabase
import com.siddharthchordia.travelassistant.model.AttractionEntity
import com.siddharthchordia.travelassistant.network.OpenTripMapApi
import javax.inject.Inject

class AttractionDetailsRepository @Inject constructor(
    private val openTripMapApi: OpenTripMapApi,
) {
    suspend fun getAttractionDetails(xid: String) = openTripMapApi.getAttractionDetails(xid)

}