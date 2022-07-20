package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GeocodingFeature (
    val type: String,
    val geometry: GeocodingGeometry,
    val properties: GeocodingProperties,
): Serializable