package com.app.skyss_companion.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteDirection (
    var publicIdentifier: String? = null,
    var direction: String? = null,
    var directionName: String? = null,
    var serviceMode: String? = null,
    var serviceMode2: String? = null,
    var identifier: String? = null,
    var passingTimes: List<PassingTime>? = null,
    var notes: List<Any>? = null
)