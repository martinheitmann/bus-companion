package com.app.skyss_companion.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopPlaceRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteWidgetService : RemoteViewsService() {

    @Inject lateinit var stopPlaceRepository: StopPlaceRepository
    @Inject lateinit var enabledWidgetRepository: EnabledWidgetRepository
    @Inject lateinit var appSharedPreferences: AppSharedPrefs

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return FavoriteRemoteViewsFactory(intent, applicationContext, stopPlaceRepository, enabledWidgetRepository, appSharedPreferences)
    }
}