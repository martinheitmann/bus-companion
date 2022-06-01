package com.app.skyss_companion.http

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.model.travelplanner.ApiTravelPlannerResponse
import com.app.skyss_companion.model.travelplanner.TravelPlannerRoot
import com.squareup.moshi.Moshi
import okhttp3.*
import okio.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TravelPlannerClient @Inject constructor(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    val tag = "TravelPlannerClient"

    private val travelPlannerResponseAdapter = moshi.adapter(ApiTravelPlannerResponse::class.java)

    @WorkerThread
    suspend fun getTravelPlan(
        fromLocation: List<Float>,
        toLocation: List<Float>,
        fromName: String,
        toName: String,
        fromStopGroupId: String,
        toStopGroupId: String,
        timeType: String,
        timestamp: String,
        modes: List<String>,
        minimumTransferTime: Int,
        maximumWalkDistance: Int
    ) : ApiTravelPlannerResponse? {
        val httpUrl =
            HttpUrl.Builder()
                .scheme("https")
                .host("skyss-reise.giantleap.no")
                .addPathSegment("v3")
                .addPathSegment("travelplans")
                .addQueryParameter("FromLocation", fromLocation.joinToString(","))
                .addQueryParameter("ToLocation", toLocation.joinToString(","))
                .addQueryParameter("FromName", fromName)
                .addQueryParameter("ToName", toName)
                .addQueryParameter("FromStopGroupID", fromStopGroupId)
                .addQueryParameter("ToStopGroupID", toStopGroupId)
                .addQueryParameter("TimeType", timeType)
                .addQueryParameter("TS", timestamp)
                .addQueryParameter("modes", modes.joinToString(","))
                .addQueryParameter("minimumTransferTime", minimumTransferTime.toString())
                .addQueryParameter("maximumWalkDistance", maximumWalkDistance.toString())
                .build()
                .toString()

        val request = Request.Builder()
            .url(httpUrl)
            .build()

        Log.d(tag, "Requesting url $request")

        return suspendCoroutine { continuation ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) continuation.resumeWithException(IOException("Unexpected code $response"))
                        val responseBody = response.body
                        if(responseBody != null){
                            val apiTravelPlannerResponse = travelPlannerResponseAdapter.fromJson(responseBody.source())
                            continuation.resume(apiTravelPlannerResponse)
                        } else continuation.resume(null)
                    }
                }
            })
        }
    }
}