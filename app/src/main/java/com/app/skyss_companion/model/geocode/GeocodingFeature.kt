package com.app.skyss_companion.model.geocode

data class GeocodingFeature (
    val type: String,
    val geometry: GeocodingGeometry,
    val properties: GeocodingProperties,
)