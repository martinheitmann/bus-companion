package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EngineResponse(
    val name: String,
    val author: String,
    val version: String,
)
