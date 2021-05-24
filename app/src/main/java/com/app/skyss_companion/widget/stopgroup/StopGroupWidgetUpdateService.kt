package com.app.skyss_companion.widget.stopgroup

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.app.skyss_companion.R
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopGroupRepository
import com.app.skyss_companion.repository.StopPlaceRepository
import com.app.skyss_companion.widget.MainAppWidgetProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class StopGroupWidgetUpdateService : JobIntentService() {
    private val TAG = "UpdateService"
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    @Inject
    lateinit var stopGroupRepository: StopGroupRepository

    @Inject
    lateinit var enabledWidgetRepository: EnabledWidgetRepository

    @Inject
    lateinit var stopPlaceRepository: StopPlaceRepository
    
    override fun onHandleWork(mIntent: Intent) {
        serviceScope.launch(Dispatchers.IO) {
            val manager = AppWidgetManager.getInstance(applicationContext)
            val appWidgetId = mIntent.extras?.get("APP_WIDGET_ID") as Int?

            if (appWidgetId != null) {
                val enabledWidget = enabledWidgetRepository.getEnabledWidget(appWidgetId)
                // We need this check to fail if the widget hasn't been added yet,
                // for instance during the config activity
                if (enabledWidget != null) {
                    val widgetStopGroup =
                        stopGroupRepository.findStopGroup(enabledWidget.stopGroupIdentifier ?: "")
                    if (widgetStopGroup != null) {
                        val stopGroupName = widgetStopGroup.description
                        val stopIdentifier = widgetStopGroup.identifier
                        val date: String = getAndFormatCurrentDate()

                        // Create intents for the adapter and refresh button
                        val intent = createAdapterIntent(stopIdentifier, appWidgetId)
                        val intentSync = createSyncIntent(appWidgetId)

                        val pendingSync = PendingIntent.getBroadcast(
                            applicationContext,
                            appWidgetId, // IMPORTANT: Use a unique request code!
                            intentSync,
                            PendingIntent.FLAG_ONE_SHOT
                        )

                        // Instantiate the RemoteViews object for the app widget layout.
                        val rv = createRemoteViews(intent, date, stopGroupName, pendingSync)
                        // If this isn't called explicitly, the widget won't refresh row column count on manual refresh
                        manager.notifyAppWidgetViewDataChanged(
                            appWidgetId,
                            R.id.widget_stopgroup_listview
                        )
                        manager.updateAppWidget(appWidgetId, rv)
                    }
                }
            }
        }
    }

    /**
     * Creates the RemoteViews to be consumed by the AppWidgetManager.
     */
    private fun createRemoteViews(
        intent: Intent,
        dateString: String,
        stopGroupName: String?,
        pendingIntentSync: PendingIntent
    ): RemoteViews {
        return RemoteViews(
            applicationContext.packageName,
            R.layout.widget_view_favorited_stopgroup
        ).apply {
            setRemoteAdapter(R.id.widget_stopgroup_listview, intent)
            setEmptyView(
                R.id.widget_stopgroup_listview,
                R.id.widget_stopgroup_tv_empty
            )
            setTextViewText(
                R.id.widget_stopgroup_tv_updated,
                "Sist oppdatert: $dateString"
            )
            if (stopGroupName != null) {
                setTextViewText(R.id.widget_stopgroup_tv_title, stopGroupName)
            }
            setOnClickPendingIntent(R.id.widget_stopgroup_refresh, pendingIntentSync)
        }
    }

    /**
     * Creates a date string in order to show the last sync timestamp.
     */
    private fun getAndFormatCurrentDate(): String {
        val pattern = "dd.MM.yyyy HH:mm:ss"
        // SimpleDateFormat behaves differently on Oreo builds and above, check version
        val simpleDateFormat: SimpleDateFormat =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat(
                    pattern, applicationContext.resources.configuration.locales.get(
                        0
                    )
                )
            } else {
                SimpleDateFormat(
                    pattern,
                    applicationContext.resources.configuration.locale
                )
            }
        return simpleDateFormat.format(Date())
    }

    /***
     * Creates the intent defining the data needed for the app widget adapter.
     */
    private fun createAdapterIntent(
        stopIdentifier: String?,
        appWidgetId: Int,
    ): Intent {
        return Intent(applicationContext, StopGroupWidgetService::class.java).apply {
            // Add the app widget ID to the intent extras.
            putExtra("STOP_IDENTIFIER", stopIdentifier ?: "")
            putExtra("APPWIDGET_ID", appWidgetId)
            // Need this line for intent filtering, otherwise list contents will be duplicated
            // due to widgets sharing factory instance.
            data = Uri.fromParts("content", stopIdentifier, null)
        }
    }

    /**
     * Creates the intent needed for the sync/refresh button to function.
     */
    private fun createSyncIntent(appWidgetId: Int): Intent {
        val intentSync = Intent(applicationContext, MainAppWidgetProvider::class.java)
        intentSync.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intentSync.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        return intentSync
    }
}