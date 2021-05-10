package com.app.skyss_companion.view.routedirection_timetable

import java.time.LocalDateTime

data class PassingTimeListItem(
    val tripIdentifier: String,
    val displayTime: String,
    val timeStamp: LocalDateTime
)
