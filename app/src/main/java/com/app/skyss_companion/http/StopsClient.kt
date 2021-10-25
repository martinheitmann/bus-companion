package com.app.skyss_companion.http

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.model.ApiStopGroupsResponse
import com.app.skyss_companion.http.model.ApiTimeTablesResponse
import com.app.skyss_companion.misc.StopGroupJsonFile
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import java.io.IOException
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject

class StopsClient @Inject constructor(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    val TAG = "StopsClient"
    private val stopsResponseJsonAdapter = moshi.adapter(ApiStopGroupsResponse::class.java)
    private val timeTablesResponseAdapter = moshi.adapter(ApiTimeTablesResponse::class.java)
    private val stopGroupsUrl = "https://skyss-reise.giantleap.no/v2/stopgroups?rows=100000"
    private val stopPlaceUrl = "https://skyss-reise.giantleap.no/v2/stopgroups/NSR%3AStopPlace%3A"
    private val timeTableUrl = "https://skyss-reise.giantleap.no/v2/timetables/et?"


    @WorkerThread
    suspend fun fetchStopGroups() : ApiStopGroupsResponse? {
        try {
            Log.d(TAG, "Requesting data from '${stopGroupsUrl}'")
            val request: Request = Builder()
                .url(stopGroupsUrl)
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val stopsResponse = stopsResponseJsonAdapter.fromJson(response.body!!.source())

                if (stopsResponse != null) {
                    return stopsResponse
                }
                return null
            }
        } catch (e: Throwable){
            Log.d(TAG, "fetchStopGroups caught exception: ${e.stackTraceToString()}")
            return null
        }
    }

    @WorkerThread
    suspend fun fetchStopPlace(identifier: String) : ApiStopGroupsResponse? {
        try {
            if (identifier.isEmpty()) {
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

                if (stopsResponse != null) {
                    return stopsResponse
                }
                return null
            }
        } catch (e: Throwable){
            Log.d(TAG, "fetchStopPlace caught exception: ${e.stackTraceToString()}")
            return null
        }
    }

    @WorkerThread
    suspend fun fetchTimeTables(stopIdentifier: String, routeDirectionIdentifier: String, date: String): ApiTimeTablesResponse? {
        try {
            if (stopIdentifier.isEmpty() || routeDirectionIdentifier.isEmpty() || date.isEmpty()) return null
            val baseUrl = timeTableUrl
            val encodedStopIdentifier = URLEncoder.encode(stopIdentifier, "utf-8")
            val encodedRouteDirectionIdentifier =
                URLEncoder.encode(routeDirectionIdentifier, "utf-8")
            val encodedDate = URLEncoder.encode(date, "utf-8")
            val fullParams =
                "StopIdentifier=${encodedStopIdentifier}&RouteDirectionIdentifier=${encodedRouteDirectionIdentifier}&Date=${encodedDate}"
            val fullUrl = baseUrl + fullParams
            Log.d(TAG, "Requesting data from '${fullUrl}'")
            val request: Request = Builder()
                .url(fullUrl)
                .build()
            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val timeTablesResponse =
                    timeTablesResponseAdapter.fromJson(response.body!!.source())
                if (timeTablesResponse != null) {
                    return timeTablesResponse
                }
                return null
            }
        } catch (e: Throwable){
            Log.d(TAG, "fetchTimeTables caught exception: ${e.stackTraceToString()}")
            return null
        }
    }
}