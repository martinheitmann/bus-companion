package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnabledWidget(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val widgetId: Int,
    val identifier: String
)
