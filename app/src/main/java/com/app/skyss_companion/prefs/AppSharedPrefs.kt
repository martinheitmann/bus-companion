package com.app.skyss_companion.prefs

import android.app.Activity
import android.content.Context
import android.util.Log
import com.app.skyss_companion.R
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class AppSharedPrefs @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val moshi: Moshi)
{
    val TAG = "AppSharedPrefs"
    private val autoSyncValueAdapter = moshi.adapter(Boolean::class.java)
    private val lastSyncedValueAdapter = moshi.adapter(LocalDateTime::class.java)

    suspend fun writeAutoSync(value: Boolean){
        val sharedPref = context.getSharedPreferences(context.getString(R.string.shared_prefs_app_file), Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            val valueAsString = autoSyncValueAdapter.toJson(value)
            putString(context.getString(R.string.shared_prefs_auto_sync), valueAsString)
            apply()
        }
    }

    suspend fun readAutoSync() : Boolean? {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.shared_prefs_app_file), Context.MODE_PRIVATE) ?: return null
        val prefsAsString = sharedPref.getString(context.getString(R.string.shared_prefs_auto_sync), null)
        return if(prefsAsString != null) autoSyncValueAdapter.fromJson(prefsAsString)
            else null
    }

    suspend fun writeLastSynced(value: LocalDateTime){
        val sharedPref = context.getSharedPreferences(context.getString(R.string.shared_prefs_app_file), Context.MODE_PRIVATE) ?: return
        Log.d(TAG, "Writing date to sharedprefs: $value")
        with (sharedPref.edit()) {
            val valueAsString = lastSyncedValueAdapter.toJson(value)
            putString(context.getString(R.string.shared_prefs_last_synced), valueAsString)
            apply()
        }
    }

    suspend fun readLastSynced() : LocalDateTime? {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.shared_prefs_app_file), Context.MODE_PRIVATE) ?: return null
        val prefsAsString = sharedPref.getString(context.getString(R.string.shared_prefs_last_synced), null)
        Log.d(TAG, "Fetching date from sharedprefs: $prefsAsString")
        return if(prefsAsString != null) lastSyncedValueAdapter.fromJson(prefsAsString)
            else return null
    }
}