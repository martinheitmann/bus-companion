package com.app.skyss_companion.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.app.skyss_companion.repository.StopPlaceRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteWidgetService : RemoteViewsService() {

    @Inject
    lateinit var stopPlaceRepository: StopPlaceRepository

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return FavoriteRemoteViewsFactory(intent, applicationContext, stopPlaceRepository)
    }
}