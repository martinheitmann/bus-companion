package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class EnabledWidget(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val widgetId: Int,
    val widgetType: WidgetType? = null,
    val stopGroupIdentifier: String? = null,
    val routeDirectionIdentifier: String? = null,
) : Serializable
