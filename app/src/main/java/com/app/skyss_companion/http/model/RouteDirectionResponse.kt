package com.app.skyss_companion.http.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteDirectionResponse(
    var PublicIdentifier: String? = null,
    var Direction: String? = null,
    var DirectionName: String? = null,
    var ServiceMode: String? = null,
    var ServiceMode2: String? = null,
    var Identifier: String? = null,
    var PassingTimes: List<PassingTimeResponse>? = null,
    var Notes: List<Any>? = null
)
