package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RecentlyUsed(
    @PrimaryKey val identifier: String,
    val timestamp: Date,
)
