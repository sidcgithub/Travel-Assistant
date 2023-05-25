package com.siddharthchordia.travelassistant.network

import com.siddharthchordia.travelassistant.BuildConfig
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.model.AttractionDetails
import com.siddharthchordia.travelassistant.model.CityInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface OpenTripMapApi {
    companion object {
        val apiKey = BuildConfig.API_KEY
    }
    @GET("radius")
    suspend fun getAttractionsInRadius(
        @Query("lang") language: String = "en",
        @Query("radius") radius: Int,
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
        @Query("format") format: String = "json",
        @Query("apikey") apiKey: String = OpenTripMapApi.apiKey
    ): List<Attraction>

    @GET("xid/{xid}")
    suspend fun getAttractionDetails(
        @Path("xid") xid: String,
        @Query("apikey") apiKey: String = OpenTripMapApi.apiKey
    ): AttractionDetails

    @GET("geoname")
    suspend fun getCityInfo(
        @Query("name") cityName: String,
        @Query("apikey") apiKey: String = OpenTripMapApi.apiKey
    ): CityInfoResponse
}