package com.siddharthchordia.travelassistant.repository

import androidx.lifecycle.LiveData
import com.siddharthchordia.travelassistant.database.CityDao
import com.siddharthchordia.travelassistant.model.City
import com.siddharthchordia.travelassistant.model.CityInfoResponse
import com.siddharthchordia.travelassistant.network.OpenTripMapApi
import java.util.Date
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val cityDao: CityDao,
    private val openTripMapApi: OpenTripMapApi
) {
    suspend fun fetchCityLatLong(cityName: String): CityInfoResponse {
        return openTripMapApi.getCityInfo(cityName)
    }

    suspend fun saveCityWithGeofenceId(city: City) {
        cityDao.insert(city)
    }

    suspend fun removeCityAndGeofence(cityId: Long) {
        cityDao.delete(cityId)
    }

    fun fetchTripList(): LiveData<List<City>> {
        return cityDao.getAllCities()
    }

    fun getCityById(cityId: Long): LiveData<City> {
        return cityDao.getCityById(cityId)
    }

    fun getCityByDate(date: Date): LiveData<List<City>> {
        return cityDao.getCityByDate(date)
    }
    fun fetchNextUpcomingCity(): LiveData<City?> {
        val currentDate = Date()
        return cityDao.fetchNextUpcomingCity(currentDate)
    }
}