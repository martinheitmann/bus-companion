package com.app.skyss_companion.prefs

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.app.skyss_companion.R
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPrefs @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi,
)
{
    val TAG = "AppSharedPrefs"
    private val autoSyncValueAdapter = moshi.adapter(Boolean::class.java)
    private val lastSyncedValueAdapter = moshi.adapter(LocalDateTime::class.java)
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    suspend fun writeAutoSync(value: Boolean){
        if(sharedPreferences == null) return
        with (sharedPreferences.edit()) {
            val valueAsString = autoSyncValueAdapter.toJson(value)
            putString(context.getString(R.string.shared_prefs_auto_sync), valueAsString)
            apply()
        }
    }

    suspend fun readAutoSync() : Boolean? {
        if(sharedPreferences == null) return null
        val prefsAsString = sharedPreferences.getString(context.getString(R.string.shared_prefs_auto_sync), null)
        return if(prefsAsString != null) autoSyncValueAdapter.fromJson(prefsAsString)
            else null
    }

    suspend fun writeLastSynced(value: LocalDateTime){
        if(sharedPreferences == null) return
        Log.d(TAG, "Writing date to sharedprefs: $value")
        with (sharedPreferences.edit()) {
            val valueAsString = lastSyncedValueAdapter.toJson(value)
            putString(context.getString(R.string.shared_prefs_last_synced), valueAsString)
            apply()
        }
    }

    suspend fun readLastSynced() : LocalDateTime? {
        if(sharedPreferences == null) return null
        val prefsAsString = sharedPreferences.getString(context.getString(R.string.shared_prefs_last_synced), null)
        Log.d(TAG, "Fetching date from sharedprefs: $prefsAsString")
        return if(prefsAsString != null) lastSyncedValueAdapter.fromJson(prefsAsString)
            else return null
    }

    suspend fun writeWidgetTimeItemsLimit(value: Int){
        if(sharedPreferences == null) return
        Log.d(TAG, "Writing date to sharedprefs: $value")
        with (sharedPreferences.edit()) {
            putInt("widget_time_items_limit", value)
            apply()
        }
    }

    suspend fun readWidgetTimeItemsLimit() : String? {
        if(sharedPreferences == null) return null
        val prefs = sharedPreferences.getString("widget_time_items_limit", null)
        Log.d(TAG, "Fetching cell num from sharedprefs: $prefs")
        return prefs
    }
}