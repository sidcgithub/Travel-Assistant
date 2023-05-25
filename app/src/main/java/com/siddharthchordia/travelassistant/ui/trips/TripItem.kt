package com.siddharthchordia.travelassistant.ui.trips

import java.util.Date

data class TripItem(
    val id: Int,
    val cityName: String,
    val startDate: Date,
    val endDate: Date,
    val geofenceId: String,
    val onRemoveTrip: () -> Unit
) {
    val startDateMessage = "Start Date: ${startDate.toString()}"
    val endDateMessage = "End Date: ${endDate.toString()}"
}