package com.app.skyss_companion.misc

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtils {
    companion object {
        const val TAG = "DateUtils"
        const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        fun isAfterNow(s: String?, formatterPattern: String): Boolean {
            if (s != null) {
                val zonedDateTime = parseUtcDateStringToEuOsloTimeZone(s)
                if (zonedDateTime.isAfter(ZonedDateTime.now())) return true
                return false
            }
            return false
        }

        fun parseUtcDateStringToEuOsloTimeZone(s: String): ZonedDateTime {
            val ldt = parseDateStringToLocalDateTime(
                s, getFormatter(
                    DATE_PATTERN
                )
            )
            val zdt1 = convertLocalDateTimeToUtcZonedDateTime(ldt)
            return changeZoneForZonedDateTime(zdt1)
        }

        fun getFormatter(pattern: String): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(pattern)
        }

        fun convertLocalDateTimeToUtcOffsetDateTime(ldt: LocalDateTime): OffsetDateTime {
            return ldt.atZone(ZoneId.of("UTC"))
                .toOffsetDateTime()
        }

        fun changeZoneForZonedDateTime(zdt: ZonedDateTime): ZonedDateTime {
            return zdt.withZoneSameInstant(ZoneId.of("Europe/Oslo"))
        }

        fun convertLocalDateTimeToUtcZonedDateTime(ldt: LocalDateTime): ZonedDateTime {
            return ldt.atZone(ZoneId.of("UTC"))
        }

        fun parseDateStringToLocalDateTime(s: String, formatter: DateTimeFormatter): LocalDateTime {
            return LocalDateTime.parse(s, formatter)
        }

        fun formatDate(s: String?, pattern: String): LocalDateTime {
            return LocalDateTime.parse(s, getFormatter(pattern))
        }

        fun parseOffsetDateTime(dateString: String?): OffsetDateTime? {
            if(dateString == null) return null
            return try {
                OffsetDateTime.parse(dateString)
            } catch (e: Throwable) {
                null
            }
        }
    }
}