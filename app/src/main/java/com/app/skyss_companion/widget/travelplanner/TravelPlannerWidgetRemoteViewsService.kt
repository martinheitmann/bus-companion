package com.app.skyss_companion.widget.travelplanner

import android.content.Intent
import android.util.Log
import android.widget.RemoteViewsService
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.TravelPlannerRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TravelPlannerWidgetRemoteViewsService : RemoteViewsService() {

    val tag = "TPRemoteViewsService"

    @Inject
    lateinit var travelPlannerRepository: TravelPlannerRepository

    @Inject
    lateinit var enabledWidgetRepository: EnabledWidgetRepository

    @Inject
    lateinit var appSharedPreferences: AppSharedPrefs

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        Log.d(tag, "creating RemoteViews factory instance.")
        return TravelPlannerWidgetRemoteViewsFactory(
            intent,
            applicationContext,
            travelPlannerRepository,
            enabledWidgetRepository,
            appSharedPreferences
        )
    }
}