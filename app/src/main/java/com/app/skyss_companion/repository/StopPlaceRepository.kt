package com.app.skyss_companion.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.StopsClient
import com.app.skyss_companion.mappers.StopResponseEntityMapper
import com.app.skyss_companion.model.StopGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopPlaceRepository @Inject constructor(private val stopsClient: StopsClient) {

    val TAG = "StopPlaceRepository"

    @WorkerThread
    suspend fun fetchStopPlace(stopIdentifier: String) : StopGroup? {
        if(stopIdentifier.isEmpty()){
            Log.d(TAG, "fetchStopPlace received an empty stop identifier")
            return null
        }

        val stopGroupFromApi = stopsClient.fetchStopPlace(stopIdentifier)
        if(stopGroupFromApi != null){
            val stopGroup = stopGroupFromApi.StopGroups
            val mappedStopGroup = StopResponseEntityMapper.mapAllStopGroupResponses(stopGroup).first()
            val stops = mappedStopGroup?.stops
            return if(mappedStopGroup != null && stops != null && stops.isNotEmpty()){
                Log.d(TAG, "Found ${stops.size} stops for stop ${mappedStopGroup.stops?.first()?.identifier}")
                return mappedStopGroup
            } else {
                Log.d(TAG, "StopGroup field 'stops' was either null or an empty list")
                null
            }
        }
        Log.d(TAG, "Remote API or JSON parser responded with null")
        return null
    }

}