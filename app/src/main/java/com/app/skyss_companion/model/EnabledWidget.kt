package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class EnabledWidget(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val widgetId: Int,
    val identifier: String,
    val minWidth: Int? = null,
    val minHeight: Int? = null,
    val maxWidth: Int? = null,
    val maxHeight: Int? = null,
) : Serializable
