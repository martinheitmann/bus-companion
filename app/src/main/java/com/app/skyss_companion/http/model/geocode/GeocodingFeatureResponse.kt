package com.app.skyss_companion.http.model.geocode


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingFeatureResponse(
    val type: String,
    val geometry: GeocodingGeometryResponse,
    val properties: GeocodingPropertiesResponse,
)