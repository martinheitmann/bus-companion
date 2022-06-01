package com.app.skyss_companion.model.travelplanner

data class TravelPlannerRoot (
    private val resultCode: String? = null,
    private val travelPlans: List<TravelPlan>
)