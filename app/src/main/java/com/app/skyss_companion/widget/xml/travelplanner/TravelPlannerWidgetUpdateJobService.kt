package com.app.skyss_companion.widget.xml.travelplanner

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.app.skyss_companion.R
import com.app.skyss_companion.broadcastreceivers.TravelPlannerWidgetBroadcastReceiver
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.widget.xml.TravelPlannerAppWidgetProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@SuppressLint("SpecifyJobSchedulerIdRange")
@AndroidEntryPoint
class TravelPlannerWidgetUpdateJobService : JobService() {

    val tag = "TPWidgetUpdateJobService"
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO) + serviceJob

    @Inject
    lateinit var enabledWidgetRepository: EnabledWidgetRepository

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        serviceScope.launch {
            Log.d(tag, "doWork invoked.")
            val manager = AppWidgetManager.getInstance(applicationContext)
            //val appWidgetId = inputData.getInt("APP_WIDGET_ID", -1)
            val extrasAppWidgetId = jobParameters?.extras?.getInt("APP_WIDGET_ID")
            extrasAppWidgetId?.let { appWidgetId ->
                Log.d(tag, "appWidgetId with value $appWidgetId defined.")
                val fetchedEnabledWidget = enabledWidgetRepository.getEnabledWidget(appWidgetId)
                // We need this check to fail if the widget hasn't been added yet,
                // for instance during the config activity
                fetchedEnabledWidget?.let { enabledWidget ->
                    Log.d(
                        tag,
                        "enabledWidget with id ${enabledWidget.id} and appWidgetId ${enabledWidget.widgetId} defined."
                    )
                    // Create intents for the adapter and refresh button
                    val intent = createAdapterIntent(enabledWidget.widgetId)
                    val intentSync = createSyncIntent(enabledWidget.widgetId)
                    val pendingSync = PendingIntent.getBroadcast(
                        applicationContext,
                        enabledWidget.widgetId, // IMPORTANT: Use a unique request code!
                        intentSync,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
                    )
                    // Instantiate the RemoteViews object for the app widget layout.
                    val rv = createRemoteViews(enabledWidget, intent, pendingSync)
                    val onClickIntent = Intent(applicationContext, TravelPlannerWidgetBroadcastReceiver::class.java)
                    val onClickPendingIntent = PendingIntent.getBroadcast(
                        applicationContext, appWidgetId, onClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                    )
                    /*val onClickPendingIntent = NavDeepLinkBuilder(applicationContext)
                        .setGraph(R.navigation.nav_graph)
                        .setDestination(R.id.selectedTravelPlanFragment)
                        .createTaskStackBuilder()
                        .getPendingIntent(
                            appWidgetId,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                        )*/

                    rv.setPendingIntentTemplate(
                        R.id.widget_travelplans_view_listview_primary,
                        onClickPendingIntent
                    )
                    // If this isn't called explicitly, the widget won't refresh row column count on manual refresh
                    manager.notifyAppWidgetViewDataChanged(
                        enabledWidget.widgetId,
                        R.id.widget_travelplans_view_listview_primary
                    )
                    manager.updateAppWidget(enabledWidget.widgetId, rv)
                } ?: run {
                    Log.d(tag, "onStartJob fetching enabled widget returned null, ending job.")
                }
            } ?: run {
                Log.d(tag, "onStartJob received an app widget id which was null, ending job.")
            }
            jobFinished(jobParameters, false)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        try {
            if (serviceScope.isActive) serviceScope.cancel()
        } catch (e: Throwable) {
            if (e is CancellationException) {
                Log.d(tag, "onStopJob scope cancelled.")
            }
        }
        return true
    }

    /**
     * Creates the RemoteViews to be consumed by the AppWidgetManager.
     */
    private fun createRemoteViews(
        enabledWidget: EnabledWidget,
        intent: Intent,
        pendingIntentSync: PendingIntent
    ): RemoteViews {
        Log.d(tag, "creating parent container RemoteViews.")
        return RemoteViews(
            applicationContext.packageName,
            R.layout.widget_travelplans_view
        ).apply {
            val fromName = enabledWidget.fromFeature?.properties?.label
            val toName = enabledWidget.toFeature?.properties?.label
            setRemoteAdapter(R.id.widget_travelplans_view_listview_primary, intent)
            setEmptyView(
                R.id.widget_travelplans_view_listview_primary,
                R.id.widget_travelplans_view_empty_placeholder
            )
            setTextViewText(
                R.id.widget_travelplans_view_tw_date,
                DateTimeFormatter
                    .ofPattern("EEE dd.MM.yyyy HH:mm")
                    .format(LocalDateTime.now())
            )
            if (fromName != null && toName != null) {
                setTextViewText(R.id.widget_travelplans_view_tw_from, fromName)
                setTextViewText(R.id.widget_travelplans_view_tw_to, toName)
            }
            setOnClickPendingIntent(
                R.id.widget_travelplans_view_imgbutton_refresh,
                pendingIntentSync
            )
        }
    }

    /***
     * Creates the intent defining the data needed for the app widget adapter.
     */
    private fun createAdapterIntent(appWidgetId: Int): Intent {
        Log.d(tag, "creating intent for RemoteViews adapter.")
        return Intent(applicationContext, TravelPlannerWidgetRemoteViewsService::class.java).apply {
            // Add the app widget ID to the intent extras.
            putExtra("APPWIDGET_ID", appWidgetId)
            // Need this line for intent filtering, otherwise list contents will be duplicated
            // due to widgets sharing factory instance.
            data = Uri.fromParts("content", appWidgetId.toString(), null)
        }
    }

    /**
     * Creates the intent needed for the sync/refresh button to function.
     */
    private fun createSyncIntent(appWidgetId: Int): Intent {
        Log.d(tag, "creating sync intent for RemoteViews.")
        val intentSync = Intent(applicationContext, TravelPlannerAppWidgetProvider::class.java)
        intentSync.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intentSync.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        return intentSync
    }

}