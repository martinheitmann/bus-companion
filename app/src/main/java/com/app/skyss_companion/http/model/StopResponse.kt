package com.app.skyss_companion.http.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StopResponse (
    var Identifier: String? = null,
    var Description: String? = null,
    var Location: String? = null,
    var ServiceModes: List<String>? = null,
    var ServiceModes2: List<String>? = null,
    var Detail: String? = null,
    var SkyssId: String? = null,
    var RouteDirections: List<RouteDirectionResponse>? = null,
    var Platform: String? = null
)