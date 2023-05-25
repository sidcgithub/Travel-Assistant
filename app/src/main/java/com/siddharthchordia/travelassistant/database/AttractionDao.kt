package com.siddharthchordia.travelassistant.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.siddharthchordia.travelassistant.model.Attraction
import com.siddharthchordia.travelassistant.model.AttractionDetails
import com.siddharthchordia.travelassistant.model.AttractionEntity

@Dao
interface AttractionDao {
    @Query("SELECT * FROM attraction_table")
    fun getAttractions(): LiveData<List<AttractionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attractions: List<AttractionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attraction: AttractionEntity)

    @Query("SELECT * FROM attraction_table WHERE done = 0")
    fun getAttractionsNotDone(): LiveData<List<AttractionEntity>>
    @Update
    suspend fun update(attraction: AttractionEntity)

    @Delete
    suspend fun delete(attraction: AttractionEntity)
    @Query("SELECT * FROM attraction_table WHERE done = 1")
    fun getAttractionsDone(): LiveData<List<AttractionEntity>>
}