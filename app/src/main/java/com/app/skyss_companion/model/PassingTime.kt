package com.app.skyss_companion.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PassingTime (
    var timestamp: String? = null,
    var tripIdentifier: String? = null,
    var status: String? = null,
    var displayTime: String? = null,
    var notes: List<Any>? = null,
    var predictionInaccurate: String? = null,
    var passed: Boolean? = null,
    var occupancy: String? = null
)