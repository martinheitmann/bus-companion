package com.app.skyss_companion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BookmarkedStopGroup(
    @PrimaryKey
    val stopGroupIdentifier: String
) : Serializable
