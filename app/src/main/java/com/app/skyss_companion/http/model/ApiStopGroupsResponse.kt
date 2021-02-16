package com.app.skyss_companion.http.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiStopGroupsResponse(
    val resultCode: String,
    val StopGroups: List<StopGroupResponse>
)
