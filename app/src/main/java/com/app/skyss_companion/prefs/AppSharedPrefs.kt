package com.app.skyss_companion.prefs

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppSharedPrefs @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
) {
    val tag = "AppSharedPrefs"
    private val autoSyncValueAdapter = moshi.adapter(Boolean::class.java)
    private val lastSyncedValueAdapter = moshi.adapter(LocalDateTime::class.java)
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val listType: Type = Types.newParameterizedType(
        MutableList::class.java,
        GeocodingFeature::class.java
    )
    private val geocodingFeaturesAdapter: JsonAdapter<List<GeocodingFeature>> =
        moshi.adapter(listType)

    suspend fun writeAutoSync(value: Boolean) {
        if (sharedPreferences == null) return
        with(sharedPreferences.edit()) {
            val valueAsString = autoSyncValueAdapter.toJson(value)
            putString(context.getString(R.string.shared_prefs_auto_sync), valueAsString)
            apply()
        }
    }

    suspend fun readAutoSync(): Boolean? {
        if (sharedPreferences == null) return null
        val prefsAsString =
            sharedPreferences.getString(context.getString(R.string.shared_prefs_auto_sync), null)
        return if (prefsAsString != null) autoSyncValueAdapter.fromJson(prefsAsString)
        else null
    }

    suspend fun writeLastSynced(value: LocalDateTime) {
        if (sharedPreferences == null) return
        Log.d(tag, "Writing date to sharedprefs: $value")
        with(sharedPreferences.edit()) {
            val valueAsString = lastSyncedValueAdapter.toJson(value)
            putString(context.getString(R.string.shared_prefs_last_synced), valueAsString)
            apply()
        }
    }

    suspend fun readLastSynced(): LocalDateTime? {
        if (sharedPreferences == null) return null
        val prefsAsString =
            sharedPreferences.getString(context.getString(R.string.shared_prefs_last_synced), null)
        Log.d(tag, "Fetching date from sharedprefs: $prefsAsString")
        return if (prefsAsString != null) lastSyncedValueAdapter.fromJson(prefsAsString)
        else return null
    }

    suspend fun writeWidgetTimeItemsLimit(value: Int) {
        if (sharedPreferences == null) return
        Log.d(tag, "Writing date to sharedprefs: $value")
        with(sharedPreferences.edit()) {
            putInt("widget_time_items_limit", value)
            apply()
        }
    }

    suspend fun readWidgetTimeItemsLimit(): String? {
        if (sharedPreferences == null) return null
        val prefs = sharedPreferences.getString("widget_time_items_limit", null)
        Log.d(tag, "Fetching cell num from sharedprefs: $prefs")
        return prefs
    }

    suspend fun writeLastUsedGeocodingFeatures(geocodingFeature: GeocodingFeature) {
        Log.d(
            tag,
            "writeLastUsedGeocodingFeatures writing feature with type ${geocodingFeature.type}."
        )
        if (sharedPreferences == null) return
        val serializedArray = sharedPreferences.getString(
            context.getString(R.string.last_used_geocoding_features),
            null
        )
        var geocodingFeatures: MutableList<GeocodingFeature> = mutableListOf()
        withContext(Dispatchers.IO) {
            serializedArray?.let { arr ->
                // Hides the annoying "inappropriate blocking call" lint
                kotlin.runCatching {
                    geocodingFeaturesAdapter.fromJson(arr)?.let { data ->
                        geocodingFeatures = data.toMutableList()
                    }
                }
            }
        }
        if (geocodingFeatures.contains(geocodingFeature)) {
            val index = geocodingFeatures.indexOf(geocodingFeature)
            geocodingFeatures.removeAt(index)
        }
        val newFeatures = listOf(geocodingFeature) + geocodingFeatures
        val limited = newFeatures.take(20)
        val serializedFeatures = geocodingFeaturesAdapter.toJson(limited)
        with(sharedPreferences.edit()) {
            putString(context.getString(R.string.last_used_geocoding_features), serializedFeatures)
            apply()
        }
    }

    suspend fun readLastUsedGeocodingFeatures(): List<GeocodingFeature>? {
        if (sharedPreferences == null) return null
        val prefsAsString =
            sharedPreferences.getString(
                context.getString(R.string.last_used_geocoding_features),
                null
            )
        Log.d(tag, "Fetching geocoding features from sharedprefs")
        var geocodingFeatures: List<GeocodingFeature>? = null
        withContext(Dispatchers.IO) {
            // Hides the annoying "inappropriate blocking call" lint
            kotlin.runCatching {
                if (prefsAsString != null) {
                    geocodingFeaturesAdapter.fromJson(prefsAsString)?.let { data ->
                        geocodingFeatures = data
                    }
                }
            }
        }
        Log.d(
            tag,
            "readLastUsedGeocodingFeatures returning list of ${geocodingFeatures?.size} elements."
        )
        return geocodingFeatures
    }

    suspend fun writeWidgetRowItemCount(rowCount: Int) {
        if (sharedPreferences == null) return
        with(sharedPreferences.edit()) {
            putInt(context.getString(R.string.widget_travelplanner_entry_row_count), rowCount)
            apply()
        }
    }

    suspend fun readWidgetRowItemCount(): Int? {
        if (sharedPreferences == null) return null
        val rowCount =
            sharedPreferences.getInt(
                context.getString(R.string.widget_travelplanner_entry_row_count),
                -1
            )
        return if(rowCount == -1) null
        else rowCount
    }
}