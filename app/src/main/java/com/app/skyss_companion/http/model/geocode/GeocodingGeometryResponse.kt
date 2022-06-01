package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingGeometryResponse(
    val type: String,
    val coordinates: List<Float>
)
