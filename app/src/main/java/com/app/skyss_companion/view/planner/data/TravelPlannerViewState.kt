package com.app.skyss_companion.view.planner.data

import com.app.skyss_companion.model.geocode.GeocodingFeature
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

data class TravelPlannerViewState(
    val departureSearchText: String = "",
    val destinationSearchText: String = "",
    var selectedDepartureFeature: GeocodingFeature? = null,
    var selectedDestinationFeature: GeocodingFeature? = null,
    var plannerTime: ZonedDateTime = ZonedDateTime.now(),
    var timeType: TravelPlannerTimeType = TravelPlannerTimeType.DEPARTURE,
)