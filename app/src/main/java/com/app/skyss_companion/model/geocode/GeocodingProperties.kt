package com.app.skyss_companion.model.geocode

data class GeocodingProperties(
    val id: String?,
    val gid: String?,
    val layer: String?,
    val source: String?,
    val sourceId: String?,
    val name: String?,
    val street: String?,
    val accuracy: String?,
    val countryA: String?,
    val county: String?,
    val countyGid: String?,
    val locality: String?,
    val localityGid: String?,
    val label: String?,
    val category: List<String>,
)
