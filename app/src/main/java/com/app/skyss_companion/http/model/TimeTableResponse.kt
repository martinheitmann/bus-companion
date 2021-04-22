package com.app.skyss_companion.http.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeTableResponse(
    val PassingTimes: List<PassingTimeResponse>? = null
)
