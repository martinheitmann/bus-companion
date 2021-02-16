package com.app.skyss_companion.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeAdapter {

    @ToJson
    fun toJson(d: LocalDateTime) : String {
        return d.toString()
    }

    @FromJson
    fun fromJson(d: String) : LocalDateTime {
        return LocalDateTime.parse(d)
    }

}