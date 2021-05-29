package com.app.skyss_companion.misc

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {
        val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        fun isAfterNow(s: String?, formatterPattern: String): Boolean {
            return LocalDateTime.parse(
                s,
                getFormatter(formatterPattern)
            ).isAfter(LocalDateTime.now())
        }

        fun getFormatter(pattern: String): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(pattern)
                .withZone(ZoneId.of("Europe/Oslo"))
        }

        fun formatDate(s: String?, pattern: String): LocalDateTime {
            return LocalDateTime.parse(s, getFormatter(pattern))
        }
    }
}