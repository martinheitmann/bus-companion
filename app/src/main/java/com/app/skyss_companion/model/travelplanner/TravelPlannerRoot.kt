package com.app.skyss_companion.model.travelplanner

data class TravelPlannerRoot(
    val resultCode: String? = null,
    val travelPlans: List<TravelPlan>
)