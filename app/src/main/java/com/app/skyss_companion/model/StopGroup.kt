package com.app.skyss_companion.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class StopGroup(
    @NonNull
    @PrimaryKey
    var identifier: String,
    var description: String? = null,
    var location: String? = null,
    var serviceModes: List<String>? = null,
    var serviceModes2: List<String>? = null,
    var stops:  List<Stop>? = null,
    var lineCodes: List<String>? = null
)
