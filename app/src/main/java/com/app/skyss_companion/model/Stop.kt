package com.app.skyss_companion.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stop (
        val identifier: String,
        val description: String? = null,
        val location: String? = null,
        val serviceModes: List<String>? = null,
        val serviceModes2: List<String>? = null,
        val detail: String? = null,
        val skyssId: String? = null,
        val routeDirections: List<RouteDirection>? = null,
)