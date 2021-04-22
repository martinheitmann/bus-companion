package com.app.skyss_companion.http.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiTimeTablesResponse (
    val resultCode: String,
    val Timetables: List<TimeTableResponse>
)