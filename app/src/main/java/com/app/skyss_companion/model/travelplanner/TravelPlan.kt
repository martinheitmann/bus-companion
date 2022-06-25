package com.app.skyss_companion.model.travelplanner

import java.time.OffsetDateTime
import java.time.ZonedDateTime

data class TravelPlan(
    val id: String? = null,
    val url: String? = null,
    val startTime: ZonedDateTime? = null,
    val endTime: ZonedDateTime? = null,
    val end: End? = null,
    val travelSteps: List<TravelStep>,
)