package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PassingTimeAlert(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val time: Date,
    val display: String,
)
