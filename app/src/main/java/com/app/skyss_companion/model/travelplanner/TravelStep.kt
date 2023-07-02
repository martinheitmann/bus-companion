package com.app.skyss_companion.model.travelplanner

import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.Stop
import java.time.OffsetDateTime
import java.time.ZonedDateTime

data class TravelStep(
    val type: String? = null,
    val startTime: ZonedDateTime? = null,
    val endTime: ZonedDateTime? = null,
    val distance: String? = null,
    val stopIdentifier: String? = null,
    val duration: String? = null,
    val status: String? = null,
    val path: String? = null,
    val tripIdentifier: String? = null,
    val callIdentifier: String? = null,
    val routeDirectionIdentifier: String? = null,
    val routeDirection: RouteDirection? = null,
    val stop: Stop? = null,
    val notes: List<Any>,
    val expectedEndTime: ZonedDateTime? = null,
    val intermediates: List<Intermediate>,
    val passed: Boolean = false,
    val occupancy: String? = null,
    val displayTime: String? = null,
    val waitDuration: String? = null,
)