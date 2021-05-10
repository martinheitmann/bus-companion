package com.app.skyss_companion.view.routedirection_timetable

import com.app.skyss_companion.model.TimeTable
import java.time.LocalDate
import java.time.LocalDateTime

data class DateTimeTable(
    val dateString: String,
    val date: LocalDateTime,
    val timeTable: TimeTable
)
