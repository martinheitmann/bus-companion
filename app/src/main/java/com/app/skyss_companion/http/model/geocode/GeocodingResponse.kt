package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    val version: String,
    val attribution: String,
    val query: QueryResponse,
    val engine: EngineResponse,
    val timestamp: Float,
)
