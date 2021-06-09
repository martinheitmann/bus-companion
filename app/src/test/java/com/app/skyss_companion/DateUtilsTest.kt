package com.app.skyss_companion

import com.app.skyss_companion.misc.DateUtils
import org.junit.Test
import org.junit.Assert
import java.time.ZoneId

class DateUtilsTest {

    // June 6th 2021, 12:00 GMT
    val DATE_STRING_1 = "2021-06-12T12:00:00.000Z"
    // June 6th 2021, 18:54 GMT
    val DATE_STRING_2 = "2021-06-12T18:54:00.000Z"
    // June 6th 2021, 23:54 GMT
    val DATE_STRING_3 = "2021-06-12T23:54:00.000Z"

    val pattern = DateUtils.DATE_PATTERN
    val formatter = DateUtils.getFormatter(pattern)




    @Test
    fun parsingDateStringReturnsCorrectLocalDateTime(){
        val ldt1 = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_1, formatter)
        Assert.assertEquals(0, ldt1.second)
        Assert.assertEquals(0, ldt1.minute)
        Assert.assertEquals(12, ldt1.hour)
        Assert.assertEquals(12, ldt1.dayOfMonth)
        Assert.assertEquals(6, ldt1.monthValue)
        Assert.assertEquals(2021, ldt1.year)

        val ldt2 = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_2, formatter)
        Assert.assertEquals(0, ldt2.second)
        Assert.assertEquals(54, ldt2.minute)
        Assert.assertEquals(18, ldt2.hour)
        Assert.assertEquals(12, ldt2.dayOfMonth)
        Assert.assertEquals(6, ldt2.monthValue)
        Assert.assertEquals(2021, ldt2.year)

        val ldt3 = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_3, formatter)
        Assert.assertEquals(0, ldt3.second)
        Assert.assertEquals(54, ldt3.minute)
        Assert.assertEquals(23, ldt3.hour)
        Assert.assertEquals(12, ldt3.dayOfMonth)
        Assert.assertEquals(6, ldt3.monthValue)
        Assert.assertEquals(2021, ldt3.year)
    }

    @Test
    fun convertedZonedDateTimeHasCorrectTimeZone(){
        val ldt = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_1, formatter)
        val zdt = DateUtils.convertLocalDateTimeToUtcZonedDateTime(ldt)
        assert(zdt.zone == ZoneId.of("UTC"))
    }

    @Test
    fun convertedZonedDateTimeHasCorrectTimeZone2(){
        val ldt = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_1, formatter)
        val zdt = DateUtils.convertLocalDateTimeToUtcZonedDateTime(ldt)
        val zdt2 = DateUtils.changeZoneForZonedDateTime(zdt)
        assert(zdt2.zone == ZoneId.of("Europe/Oslo"))
    }

    @Test
    fun changedZonedDateTimeHasCorrectDate(){
        val ldt1 = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_1, formatter)
        val zdt1 = DateUtils.convertLocalDateTimeToUtcZonedDateTime(ldt1)
        val zdt11 = DateUtils.changeZoneForZonedDateTime(zdt1)
        Assert.assertEquals(0, zdt11.second)
        Assert.assertEquals(0, zdt11.minute)
        Assert.assertEquals(14, zdt11.hour)
        Assert.assertEquals(12, zdt11.dayOfMonth)
        Assert.assertEquals(6, zdt11.monthValue)
        Assert.assertEquals(2021, zdt11.year)

        val ldt2 = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_2, formatter)
        val zdt2 = DateUtils.convertLocalDateTimeToUtcZonedDateTime(ldt2)
        val zdt22 = DateUtils.changeZoneForZonedDateTime(zdt2)
        Assert.assertEquals(0, zdt22.second)
        Assert.assertEquals(54, zdt22.minute)
        Assert.assertEquals(20, zdt22.hour)
        Assert.assertEquals(12, zdt22.dayOfMonth)
        Assert.assertEquals(6, zdt22.monthValue)
        Assert.assertEquals(2021, zdt22.year)

        val ldt3 = DateUtils.parseDateStringToLocalDateTime(DATE_STRING_3, formatter)
        val zdt3 = DateUtils.convertLocalDateTimeToUtcZonedDateTime(ldt3)
        val zdt33 = DateUtils.changeZoneForZonedDateTime(zdt3)
        Assert.assertEquals(0, zdt33.second)
        Assert.assertEquals(54, zdt33.minute)
        Assert.assertEquals(1, zdt33.hour)
        Assert.assertEquals(13, zdt33.dayOfMonth)
        Assert.assertEquals(6, zdt33.monthValue)
        Assert.assertEquals(2021, zdt33.year)
    }
}