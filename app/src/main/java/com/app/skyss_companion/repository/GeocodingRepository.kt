package com.app.skyss_companion.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.GeocodingClient
import com.app.skyss_companion.mappers.GeocodeEntityMapper
import com.app.skyss_companion.model.geocode.GeocodingRoot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocodingRepository @Inject constructor(private val geocodingClient: GeocodingClient){
    val tag = "GeocodingRepository"

    @WorkerThread
    suspend fun autocompleteGeocodeLocationText(text: String): GeocodingRoot? {
        try {
            val apiResponse = geocodingClient.autocompleteGeocode(text)
            if(apiResponse != null) {
                return GeocodeEntityMapper.mapApiGeocodingResponse(apiResponse)
            }
            return null
        } catch(e: Throwable){
            Log.d(tag, e.stackTraceToString())
            return null
        }
    }

}