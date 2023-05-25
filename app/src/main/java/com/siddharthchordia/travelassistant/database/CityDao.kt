package com.siddharthchordia.travelassistant.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.siddharthchordia.travelassistant.model.City
import java.util.Date

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * FROM city_table")
    fun getAllCities(): LiveData<List<City>>

    @Query("SELECT * FROM city_table WHERE id = :cityId")
    fun getCityById(cityId: Long): LiveData<City>

    @Query("SELECT * FROM city_table WHERE startDate = :date")
    fun getCityByDate(date: Date): LiveData<List<City>>

    @Query("DELETE FROM city_table WHERE id = :cityId")
    suspend fun delete(cityId: Long)

    @Query("SELECT * FROM city_table WHERE startDate > (SELECT endDate FROM city_table WHERE startDate < :currentDate AND endDate > :currentDate) ORDER BY startDate LIMIT 1")
    fun fetchNextUpcomingCity(currentDate: Date): LiveData<City?>
}
