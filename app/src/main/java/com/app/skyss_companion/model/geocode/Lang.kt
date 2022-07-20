package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Lang(
    val name: String,
    val iso6391: String,
    val iso6393: String,
    val defaulted: Boolean,
)
