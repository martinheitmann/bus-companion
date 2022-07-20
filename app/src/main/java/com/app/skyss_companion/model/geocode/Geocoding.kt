package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Geocoding(
    val version: String,
    val attribution: String,
    val query: Query,
    val engine: Engine,
    val timestamp: Float,
)
