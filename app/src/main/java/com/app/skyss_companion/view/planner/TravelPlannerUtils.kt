package com.app.skyss_companion.view.planner

import com.app.skyss_companion.model.travelplanner.TravelPlan
import java.time.temporal.ChronoUnit

class TravelPlannerUtils {
    companion object {
        fun getTimeDurationString(travelPlan: TravelPlan): String {
            val start = travelPlan.startTime
            val end = travelPlan.endTime
            if (start != null && end != null) {
                val localStart = start.toLocalTime()
                val localEnd = end.toLocalTime()
                return "${localStart.hour}:${localStart.minute} - ${localEnd.hour}:${localStart.minute}"
            }
            return "Ukjent varighet..."
        }

        fun getDurationMinutes(travelPlan: TravelPlan): String {
            val start = travelPlan.startTime
            val end = travelPlan.endTime
            if (start != null && end != null) {
                val localStart = start.toLocalDateTime()
                val localEnd = end.toLocalDateTime()
                val duration = ChronoUnit.MINUTES.between(localStart, localEnd)
                return "(${duration.toInt()} minutter)"
            }
            return "N/A"
        }
    }
}