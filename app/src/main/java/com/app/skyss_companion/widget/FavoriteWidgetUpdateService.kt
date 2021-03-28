package com.app.skyss_companion.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.app.skyss_companion.R
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopGroupRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
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
            val refresh = mIntent.extras?.get("APP_WIDGET_REFRESH") as Boolean?

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
                        var widthBlocks: Int? = null

                        val pattern = "dd.MM.yyyy HH:mm:ss"
                        // SimpleDateFormat behaves differently on Oreo builds and above, check version
                        val simpleDateFormat: SimpleDateFormat = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
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
                        val date: String = simpleDateFormat.format(Date())
                        Log.d(TAG, "Current date: $date")

                        if(mIntent.extras?.get("OPTION_APPWIDGET_MIN_WIDTH") != null){
                            val minWidthDp = mIntent.extras?.get("OPTION_APPWIDGET_MIN_WIDTH") as Int
                            widthBlocks = WidgetUtils.limitDisplayedItems(minWidthDp, 0)
                            Log.d(TAG, "Min Width Blocks: $widthBlocks")
                        }

                        // Create the intent for the adapter
                        val intent = Intent(applicationContext, FavoriteWidgetService::class.java).apply {
                            // Add the app widget ID to the intent extras.
                            putExtra("STOP_IDENTIFIER", stopIdentifier ?: "")
                            putExtra("APPWIDGET_ID", appWidgetId)
                            if(widthBlocks != null) putExtra(
                                "OPTION_APPWIDGET_MIN_BLOCKS",
                                widthBlocks
                            )
                        }

                        val intentSync = Intent(applicationContext, FavoritesListWidgetProvider::class.java)
                        intentSync.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                        intentSync.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

                        val pendingSync = PendingIntent.getBroadcast(
                            applicationContext,
                            appWidgetId, // IMPORTANT: Use a unique request code!
                            intentSync,
                            PendingIntent.FLAG_ONE_SHOT
                        )

                        // Instantiate the RemoteViews object for the app widget layout.
                        val rv = RemoteViews(
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
                                "Sist oppdatert: $date"
                            )
                            if(stopGroupName != null){
                                setTextViewText(R.id.widget_stopgroup_tv_title, stopGroupName)
                            }
                            setOnClickPendingIntent(R.id.widget_stopgroup_refresh, pendingSync)
                        }
                        // If this isn't called explicitly, the widget won't refresh row column count on manual refresh
                        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_stopgroup_listview)
                        manager.updateAppWidget(appWidgetId, rv)
                    }
                }
            }
        }
    }
}