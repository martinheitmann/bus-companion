package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GeocodingGeometry(
    val type: String,
    val coordinates: List<Float>
): Serializable
