package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingPropertiesResponse(
    val id: String?,
    val gid: String?,
    val layer: String?,
    val source: String?,
    val source_id: String?,
    val name: String?,
    val street: String?,
    val accuracy: String?,
    val country_a: String?,
    val county: String?,
    val county_gid: String?,
    val locality: String?,
    val locality_gid: String?,
    val label: String?,
    val category: List<String>,
)
