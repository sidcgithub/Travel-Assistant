package com.siddharthchordia.travelassistant.model


data class Attraction(
    val name: String?,
    val kind: String?,
    val xid: String,
    val point: Point
)

data class Point(
    val lon: Double,
    val lat: Double
)