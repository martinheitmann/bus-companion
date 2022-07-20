package com.app.skyss_companion.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Query(
    val text: String,
    val parser: String,
    val tokens: List<Any>,
    val size: Float,
    val layers: List<Any>,
    val sources: List<Any>,
    val private: Boolean,
    val lang: Lang,
    val querySize: Float,
)
