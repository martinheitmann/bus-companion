package com.app.skyss_companion.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.TravelPlannerClient
import com.app.skyss_companion.mappers.TravelPlannerEntityMapper
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import com.app.skyss_companion.model.travelplanner.TravelPlannerRoot
import com.app.skyss_companion.view.planner.TravelPlannerUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelPlannerRepository @Inject constructor(private val travelPlannerClient: TravelPlannerClient) {

    val classTag = "REPO-540201"

    @WorkerThread
    suspend fun getTravelPlans(
        fromFeature: GeocodingFeature,
        toFeature: GeocodingFeature,
        timeType: String, // DEPARTURE || ARRIVAL
        timestamp: String,
        modes: List<String>,
        mtt: Int, // Minimum transfer time
        mwd: Int // Maximum walking distance
    ): TravelPlannerRoot? {
        Log.d(classTag, "[getTravelPlans] received timestamp $timestamp as argument.")
        val fromCoord1 = fromFeature.geometry.coordinates.last()
        val fromCoord2 = fromFeature.geometry.coordinates.first()
        val fromName = fromFeature.properties.label!!
        val fromId = fromFeature.properties.id!!

        val toCoord1 = toFeature.geometry.coordinates.last()
        val toCoord2 = toFeature.geometry.coordinates.first()
        val toName = toFeature.properties.label!!
        val toId = toFeature.properties.id!!

        val apiResponse = travelPlannerClient.getTravelPlans(
            fromLocation = listOf(fromCoord1, fromCoord2),
            toLocation = listOf(toCoord1, toCoord2),
            fromName = fromName,
            toName = toName,
            fromStopGroupId = fromId,
            toStopGroupId = toId,
            timeType = timeType,
            timestamp = timestamp,
            modes = modes,
            minimumTransferTime = mtt,
            maximumWalkDistance = mwd
        )
        apiResponse?.let {
            return TravelPlannerEntityMapper.mapApiTravelPlannerResponse(it)
        }
        return null
    }

    @WorkerThread
    suspend fun getTravelPlanById(id: String): TravelPlannerRoot? {
        Log.d(classTag, "[getTravelPlanById] attempting to fetch travel plan with id $id.")
        val apiResponse = travelPlannerClient.getTravelPlanById(id)
        apiResponse?.let {
            return TravelPlannerEntityMapper.mapApiTravelPlannerResponse(it)
        }
        return null
    }

    @WorkerThread
    suspend fun getTravelPlanByUrl(url: String): TravelPlannerRoot? {
        Log.d(classTag, "[getTravelPlanById] attempting to fetch travel plan from $url.")
        val apiResponse = travelPlannerClient.getTravelPlanByUrl(url)
        apiResponse?.let {
            return TravelPlannerEntityMapper.mapApiTravelPlannerResponse(it)
        }
        return null
    }

    @WorkerThread
    suspend fun getTravelPlansFromWidgetConfig(data: EnabledWidget): TravelPlannerRoot? {
        val fromFeature = data.fromFeature
        val toFeature = data.toFeature
        val timeType = data.timeType
        //val timestamp = data.timestamp
        val timestamp = toTimestampString(LocalDateTime.now())
        val modes = data.modes
        val mtt = data.minimumTransferTime
        val mwd = data.maximumWalkDistance
        if (fromFeature != null && toFeature != null && timeType != null && timestamp != null && modes != null && mtt != null && mwd != null)
            return getTravelPlans(
                fromFeature,
                toFeature,
                timeType,
                timestamp,
                modes,
                mtt,
                mwd
            )
        return null
    }

    fun getTravelPlanById2(): TravelPlannerRoot? {
        return TravelPlannerEntityMapper.mapApiTravelPlannerResponse(travelPlannerClient.getTravelPlanString1()!!)
    }

    fun getTravelPlans2(): TravelPlannerRoot? {
        return TravelPlannerEntityMapper.mapApiTravelPlannerResponse(travelPlannerClient.getTravelPlansString1()!!)
    }

    private fun toTimestampString(ldt: LocalDateTime): String {
        val asZonedTime = ZonedDateTime.of(ldt, ZoneId.of("Europe/Oslo"))
        val asUtc = asZonedTime.withZoneSameInstant( ZoneId.of("UTC") )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return formatter.format(asUtc)
    }

}