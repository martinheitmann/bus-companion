package com.app.skyss_companion.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.app.skyss_companion.R
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopGroupRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteWidgetUpdateService : JobIntentService() {
    val TAG = "UpdateService"
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    @Inject
    lateinit var stopGroupRepository: StopGroupRepository

    @Inject
    lateinit var enabledWidgetRepository: EnabledWidgetRepository


    override fun onHandleWork(mIntent: Intent) {
        serviceScope.launch(Dispatchers.IO) {
            Log.d(TAG, "UpdateService started")
            val manager = AppWidgetManager.getInstance(applicationContext)
            val appWidgetId = mIntent.extras?.get("APP_WIDGET_ID") as Int?

            if(appWidgetId != null){
                val enabledWidget = enabledWidgetRepository.getEnabledWidget(appWidgetId)
                // We need this check to fail if the widget hasn't been added yet,
                // for instance during the config activity
                if(enabledWidget != null){
                    val widgetStopGroup = stopGroupRepository.findStopGroup(enabledWidget.identifier)
                    if(widgetStopGroup != null){
                        Log.d(TAG, "StopGroup fetched: $widgetStopGroup")
                        val stopGroupName = widgetStopGroup.description
                        val stopIdentifier = widgetStopGroup.identifier

                        val intent = Intent(applicationContext, FavoriteWidgetService::class.java).apply {
                            // Add the app widget ID to the intent extras.
                            putExtra("STOP_IDENTIFIER", stopIdentifier ?: "")
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        }
                        // Instantiate the RemoteViews object for the app widget layout.
                        val rv = RemoteViews(applicationContext.packageName, R.layout.widget_view_favorited_stopgroup).apply {
                            setRemoteAdapter(R.id.widget_stopgroup_listview, intent)
                            setEmptyView(R.id.widget_stopgroup_listview, R.id.widget_stopgroup_tv_empty)
                            if(stopGroupName != null){
                                setTextViewText(R.id.widget_stopgroup_tv_title, stopGroupName)
                            }
                        }
                        manager.updateAppWidget(appWidgetId, rv)
                    }
                }
            }
        }
    }
}