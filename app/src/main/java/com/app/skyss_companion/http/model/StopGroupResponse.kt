package com.app.skyss_companion.http.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StopGroupResponse (
    val Identifier: String? = null,
    val Description: String? = null,
    val Location: String? = null,
    val ServiceModes: List<String>? = null,
    val ServiceModes2: List<String>? = null,
    val Stops:  List<StopResponse>? = null,
    val LineCodes: List<String>? = null
)