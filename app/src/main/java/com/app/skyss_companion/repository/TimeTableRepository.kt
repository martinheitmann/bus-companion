package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.StopsClient
import com.app.skyss_companion.mappers.StopResponseEntityMapper
import com.app.skyss_companion.model.TimeTable
import com.app.skyss_companion.view.routedirection_timetable.DateTimeTable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeTableRepository @Inject constructor(private val stopsClient: StopsClient) {

    @WorkerThread
    suspend fun fetchTimeTables(
        stopIdentifier: String,
        routeDirectionIdentifier: String,
        numberOfDays: Int
    ): List<DateTimeTable> {
        var dateTimeTables = emptyList<DateTimeTable>()
        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for(i in 0..numberOfDays){
            val futureDate = currentDate.plusDays(i.toLong())
            val futureDateString: String = futureDate.format(formatter)
            val apiTimeTablesResponse = stopsClient.fetchTimeTables(
                stopIdentifier,
                routeDirectionIdentifier,
                futureDateString
            )
            apiTimeTablesResponse?.Timetables?.let { apiTimeTables ->
                val timeTables = StopResponseEntityMapper.mapAllTimeTables(apiTimeTables)
                if (timeTables.isNotEmpty()) {
                    timeTables.first()?.let { timeTable ->
                        val dateTimeTable = DateTimeTable(
                            date = futureDate,
                            dateString = futureDateString,
                            timeTable = timeTable
                        )
                        dateTimeTables = dateTimeTables + dateTimeTable
                    }
                }
            }

        }
        return dateTimeTables
    }
}