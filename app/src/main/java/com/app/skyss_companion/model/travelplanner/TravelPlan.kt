package com.app.skyss_companion.model.travelplanner

import java.time.OffsetDateTime

data class TravelPlan(
    val id: String? = null,
    val url: String? = null,
    val startTime: OffsetDateTime? = null,
    val endTime: OffsetDateTime? = null,
    val end: End? = null,
    val travelSteps: List<TravelStep>,
)