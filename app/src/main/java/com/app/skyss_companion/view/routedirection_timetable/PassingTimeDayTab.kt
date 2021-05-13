package com.app.skyss_companion.view.routedirection_timetable

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

data class PassingTimeDayTab(
    val time: LocalDateTime,
    val display: String,
    val isSelected: Boolean
)
