package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.skyss_companion.model.geocode.GeocodingFeature
import java.io.Serializable

@Entity
data class EnabledWidget(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val widgetId: Int,
    val widgetType: WidgetType? = null,
    val stopGroupIdentifier: String? = null,
    val routeDirectionIdentifier: String? = null,
    // Travel planner
    val fromFeature: GeocodingFeature? = null,
    val toFeature: GeocodingFeature? = null,
    val timeType: String? = null, // DEPARTURE || ARRIVAL
    val timestamp: String? = null,
    val modes: List<String>? = null,
    val minimumTransferTime: Int? = null,
    val maximumWalkDistance: Int? = null,
) : Serializable
