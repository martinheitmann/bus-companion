package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.StopsClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TimeTableRepository @Inject constructor(private val stopsClient: StopsClient) {

    @WorkerThread
    suspend fun fetchTimeTables(stopIdentifier: String, routeDirectionIdentifier: String){
        var requestDateStrings = emptyList<String>()
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for(i in 0..6){
            val futureDate = currentDate.plusDays(i.toLong())
            val futureDateString: String = futureDate.format(formatter)
            requestDateStrings = requestDateStrings + futureDateString
        }
        if(requestDateStrings.isNotEmpty()){
            // TODO: Map timetables and return.
        }
    }
}