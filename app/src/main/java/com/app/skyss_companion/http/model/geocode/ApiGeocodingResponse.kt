package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGeocodingResponse(
    val geocoding: GeocodingResponse,
    val type: String,
    val features: List<GeocodingFeatureResponse>,
    val bbox: List<Float>,
)
