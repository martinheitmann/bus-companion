package com.app.skyss_companion.http.model.travelplanner

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IntermediateResponse(
    val StopName: String? = null,
    val Status: String? = null,
    val AimedTime: String? = null
)