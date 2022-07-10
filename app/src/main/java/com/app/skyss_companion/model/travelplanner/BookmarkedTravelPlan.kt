package com.app.skyss_companion.model.travelplanner

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.skyss_companion.model.geocode.GeocodingFeature
import java.time.LocalDateTime

@Entity
data class BookmarkedTravelPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val fromFeature: GeocodingFeature,
    val toFeature: GeocodingFeature,
    val timeType: String, // DEPARTURE || ARRIVAL
    val timestamp: String,
    val modes: List<String>,
    val minimumTransferTime: Int, // Minimum transfer time
    val maximumWalkDistance: Int, // Maximum walking distance
    val createdAt: LocalDateTime
)
