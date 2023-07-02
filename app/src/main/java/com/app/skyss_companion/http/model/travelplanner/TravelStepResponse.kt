package com.app.skyss_companion.http.model.travelplanner

import com.app.skyss_companion.http.model.RouteDirectionResponse
import com.app.skyss_companion.http.model.StopResponse

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TravelStepResponse(
    val Type: String? = null,
    val StartTime: String? = null,
    val EndTime: String? = null,
    val Distance: String? = null,
    val StopIdentifier: String? = null,
    val Duration: String? = null,
    val Status: String? = null,
    val Path: String? = null,
    val WaitDuration: String? = null,
    val TripIdentifier: String? = null,
    val CallIdentifier: String? = null,
    val RouteDirectionIdentifier: String? = null,
    val RouteDirection: RouteDirectionResponse? = null,
    val Stop: StopResponse? = null,
    val Notes: List<Any>? = null,
    val ExpectedEndTime: String? = null,
    val Intermediates: List<IntermediateResponse>? = null,
    val Passed: Boolean = false,
    val Occupancy: String? = null,
    val DisplayTime: String? = null,
)