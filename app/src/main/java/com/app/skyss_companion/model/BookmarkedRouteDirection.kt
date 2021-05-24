package com.app.skyss_companion.model

import androidx.room.Entity
import java.io.Serializable

@Entity(primaryKeys = ["routeDirectionIdentifier", "stopGroupIdentifier"])
data class BookmarkedRouteDirection(
    val routeDirectionIdentifier: String,
    val stopGroupIdentifier: String,
    val stopGroupName: String,
    val routeDirectionName: String,
    val lineCode: String
) : Serializable
