package com.app.skyss_companion.widget.xml.travelplanner

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.skyss_companion.R
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.view.planner.selected_plan.SelectedTravelPlanFragment
import com.app.skyss_companion.widget.xml.TravelPlannerAppWidgetProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@Deprecated("Replaced by corresponding job service.")
@HiltWorker
class TravelPlannerWidgetUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    private val tag = "TPWidgetUpdWorker"

    @Inject
    lateinit var enabledWidgetRepository: EnabledWidgetRepository

    override suspend fun doWork(): Result {
        Log.d(tag, "doWork invoked.")
        val manager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetId = inputData.getInt("APP_WIDGET_ID", -1)
        val enabledWidget = enabledWidgetRepository.getEnabledWidget(appWidgetId)
        // We need this check to fail if the widget hasn't been added yet,
        // for instance during the config activity
        if (enabledWidget != null && appWidgetId >= 0) {
            Log.d(tag, "enabledWidget and appWidgetId was defined.")
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
            // If this isn't called explicitly, the widget won't refresh row column count on manual refresh

            val onClickIntent = Intent(applicationContext, SelectedTravelPlanFragment::class.java)
            val onClickPendingIntent = PendingIntent.getActivity(
                applicationContext, 0, onClickIntent,
                FLAG_IMMUTABLE and PendingIntent.FLAG_UPDATE_CURRENT
            )
            rv.setPendingIntentTemplate(R.id.widget_travelplans_view_listview_primary, onClickPendingIntent)
            manager.notifyAppWidgetViewDataChanged(
                enabledWidget.widgetId,
                R.id.widget_stopgroup_listview
            )
            manager.updateAppWidget(enabledWidget.widgetId, rv)
            return Result.Success()

        } else {
            Log.d(tag, "doWork failure returned.")
            return Result.Failure()
        }
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