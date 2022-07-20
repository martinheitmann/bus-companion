package com.app.skyss_companion.http

import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.model.ApiTimeTablesResponse
import com.app.skyss_companion.http.model.geocode.ApiGeocodingResponse
import com.squareup.moshi.Moshi
import okhttp3.*
import okio.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class GeocodingClient @Inject constructor(
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    private val geocodeResponseAdapter = moshi.adapter(ApiGeocodingResponse::class.java)
    //private val baseUrl = "https://api.entur.io/geocoder/v1/autocomplete"

    @WorkerThread
    suspend fun autocompleteGeocode(text: String): ApiGeocodingResponse? {
        val httpUrl =
            HttpUrl.Builder()
                .scheme("https")
                .host("api.entur.io")
                .addPathSegment("geocoder")
                .addPathSegment("v1")
                .addPathSegment("autocomplete")
                .addQueryParameter("lang", "no")
                .addQueryParameter("text", text)
                .build()
                .toString()

        val request = Request.Builder()
            .url(httpUrl)
            .build()

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
                            val apiGeocodingResponse = geocodeResponseAdapter.fromJson(responseBody.source())
                            continuation.resume(apiGeocodingResponse)
                        } else continuation.resume(null)
                    }
                }
            })
        }
    }

}