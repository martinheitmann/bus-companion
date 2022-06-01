package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LangResponse(
    val name: String,
    val iso6391: String,
    val iso6393: String,
    val defaulted: Boolean,
)
