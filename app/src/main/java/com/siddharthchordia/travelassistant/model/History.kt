package com.siddharthchordia.travelassistant.model

import java.util.Date

data class History(
    val id: String,
    val attractionId: String,
    val dateVisited: Date
)

