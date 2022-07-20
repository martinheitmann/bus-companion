package com.app.skyss_companion.http.model.geocode

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QueryResponse(
    val text: String,
    val parser: String,
    val tokens: List<Any>,
    val size: Float,
    val layers: List<Any>,
    val sources: List<Any>,
    val private: Boolean,
    val lang: LangResponse,
    val querySize: Float,
)
