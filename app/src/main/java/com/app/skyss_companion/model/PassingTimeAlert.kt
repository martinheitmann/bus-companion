package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

@Entity
data class PassingTimeAlert(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    val zonedRouteTimestamp: ZonedDateTime,
    val zonedAlertTimestamp: ZonedDateTime,
    val tripIdentifier: String,
    val stopIdentifier: String,
    val routeDirectionIdentifier: String,
    val stopName: String,
    val directionName: String,
    val lineNumber: String,
    val departureHour: Int,
    val departureMinute: Int,
)
