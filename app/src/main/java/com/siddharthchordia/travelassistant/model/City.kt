package com.siddharthchordia.travelassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val startDate: Date,
    val endDate: Date,
    val lat: Double,
    val lon: Double,
    val label: String = "Upcoming: $name"
) {

}