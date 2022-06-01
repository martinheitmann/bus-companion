package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingRoot(
    val geocoding: Geocoding,
    val type: String,
    // https://github.com/pelias/documentation/blob/master/response.md
    val features: List<GeocodingFeature>,
    val bbox: List<Float>,
)
