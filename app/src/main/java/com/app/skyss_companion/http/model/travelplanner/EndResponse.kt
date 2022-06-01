package com.app.skyss_companion.http.model.travelplanner

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EndResponse(
    val Description: String? = null,
    val Location: String? = null,
)