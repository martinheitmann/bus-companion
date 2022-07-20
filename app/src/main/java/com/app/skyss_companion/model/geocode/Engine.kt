package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Engine(
    val name: String,
    val author: String,
    val version: String,
)
