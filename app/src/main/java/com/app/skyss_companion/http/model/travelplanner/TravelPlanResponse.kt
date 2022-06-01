package com.app.skyss_companion.http.model.travelplanner

import java.time.OffsetDateTime
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TravelPlanResponse(
    val id: String? = null,
    val url: String? = null,
    val StartTime: String? = null,
    val EndTime: String? = null,
    val End: EndResponse? = null,
    val TravelSteps: List<TravelStepResponse>? = null,
)