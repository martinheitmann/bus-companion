package com.app.skyss_companion.http.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PassingTimeResponse(
    var Timestamp: String? = null,
    var TripIdentifier: String? = null,
    var Status: String? = null,
    var DisplayTime: String? = null,
    var Notes: List<Any>? = null,
    var PredictionInaccurate: String? = null,
    var Passed: Boolean? = null
)
