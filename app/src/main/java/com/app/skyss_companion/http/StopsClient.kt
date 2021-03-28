package com.app.skyss_companion.http

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.model.ApiStopGroupsResponse
import com.app.skyss_companion.misc.StopGroupJsonFile
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import java.io.IOException
import javax.inject.Inject

class StopsClient @Inject constructor(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    val TAG = "StopsClient"
    private val stopsResponseJsonAdapter = moshi.adapter(ApiStopGroupsResponse::class.java)
    private val stopGroupsUrl = "https://skyss-reise.giantleap.no/v2/stopgroups?rows=100000"
    private val stopPlaceUrl = "https://skyss-reise.giantleap.no/v2/stopgroups/NSR%3AStopPlace%3A"


    @WorkerThread
    suspend fun fetchStopGroups() : ApiStopGroupsResponse? {
        Log.d(TAG, "Requesting data from '${stopGroupsUrl}'")
        val request: Request = Builder()
            .url(stopGroupsUrl)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val stopsResponse = stopsResponseJsonAdapter.fromJson(response.body!!.source())

            if(stopsResponse != null){
                return stopsResponse
            }
            return null
        }
    }

    @WorkerThread
    suspend fun fetchStopPlace(identifier: String) : ApiStopGroupsResponse? {
        if(identifier.isEmpty()){
            Log.d(TAG, "fetchStopPlace received an empty identifier")
            return null
        }
        Log.d(TAG, "fetchStopPlace received identifier $identifier")
        val splitIdentifierArray = identifier.split(":").toTypedArray()
        val splitIdentifier = splitIdentifierArray.last()

        val fullUrl = stopPlaceUrl + splitIdentifier
        Log.d(TAG, "Requesting data from '${fullUrl}'")
        val request: Request = Builder()
                .url(fullUrl)
                .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val stopsResponse = stopsResponseJsonAdapter.fromJson(response.body!!.source())

            if(stopsResponse != null){
                return stopsResponse
            }
            return null
        }
    }

    @WorkerThread
    suspend fun fetchStopPlaceTest(identifier: String) : ApiStopGroupsResponse? {
        val stopsResponse = stopsResponseJsonAdapter.fromJson(StopGroupJsonFile.getJson())
        if(stopsResponse != null){
            return stopsResponse
        }
        return null
    }

    /*companion object {
        private var INSTANCE: StopsClient? = null

        fun getClient(): StopsClient {
            return INSTANCE ?: synchronized(this) {
                val instance = StopsClient()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }*/
}