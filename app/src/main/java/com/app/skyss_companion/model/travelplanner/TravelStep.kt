package com.app.skyss_companion.model.travelplanner

import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.Stop
import java.time.OffsetDateTime

data class TravelStep(
    val type: String? = null,
    val startTime: OffsetDateTime? = null,
    val endTime: OffsetDateTime? = null,
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
    val expectedEndTime: OffsetDateTime? = null,
    val intermediates: List<Intermediate>,
    val passed: Boolean = false,
    val occupancy: String? = null,
    val displayTime: String? = null,
)