package com.siddharthchordia.travelassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attraction_table")
data class AttractionEntity(
    @PrimaryKey(autoGenerate = false)
    var xid: String,
    var name: String?,
    var done: Boolean = false
)

