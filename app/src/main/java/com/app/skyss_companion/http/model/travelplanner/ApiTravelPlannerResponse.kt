package com.app.skyss_companion.http.model.travelplanner

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiTravelPlannerResponse (
    val resultCode: String? = null,
    val TravelPlans: List<TravelPlanResponse>? = null
)