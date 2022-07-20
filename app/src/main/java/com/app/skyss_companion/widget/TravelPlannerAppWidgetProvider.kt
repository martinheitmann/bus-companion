package com.app.skyss_companion.widget

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat
import androidx.work.*
import com.app.skyss_companion.widget.stopgroup.StopGroupWidgetUpdateService
import com.app.skyss_companion.widget.travelplanner.TravelPlannerWidgetUpdateJobService
import com.app.skyss_companion.widget.travelplanner.TravelPlannerWidgetUpdateWorker
import com.app.skyss_companion.workers.RemoveWidgetsWorker
import com.app.skyss_companion.workers.UpdateEnabledWidgetConfigWorker

class TravelPlannerAppWidgetProvider : AppWidgetProvider() {
    val TAG = "TravelPlannerAppWidgetProvider"

    // We have to invoke this in order to update the widget from
    // a button on the app widget.
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        try {
            Log.d(TAG, "onReceive called")
            if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
                val appWidgetId = intent.extras?.get(AppWidgetManager.EXTRA_APPWIDGET_ID) as Int?
                if (appWidgetId != null && context != null) {
                    Log.d(TAG, "Updating widget with id $appWidgetId")
                    updateAppWidget(context, appWidgetId)
                }
            }
        } catch (e: Throwable) {
            Log.d(TAG, "onReceive caught exception: ${e.stackTraceToString()}")
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        try {
            Log.d(TAG, "onUpdate called for widgets with ids: ${appWidgetIds.contentToString()}")
            // There may be multiple widgets active, so update all of them
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetId, refresh = true)
            }
        } catch (e: Throwable) {
            Log.d(TAG, "onUpdate caught exception: ${e.stackTraceToString()}")
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled called")
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled called")
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted called")
        if (appWidgetIds != null && context != null) {
            Log.d(TAG, "onDeleted appWidget ids received: ${appWidgetIds.contentToString()}")
            val data = workDataOf("appWidgetIds" to appWidgetIds)
            val clearWidgetsRequest: WorkRequest = OneTimeWorkRequestBuilder<RemoveWidgetsWorker>()
                .setInputData(data)
                .build()

            WorkManager
                .getInstance(context)
                .enqueue(clearWidgetsRequest)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        if (context != null) {
            val widgetOptions = appWidgetManager?.getAppWidgetOptions(appWidgetId)
            val width = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
            val height = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
            val mWidth = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val mHeight = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            Log.d(TAG, "onAppWidgetOptionsChanged max width/height: $width/$height")
            Log.d(TAG, "onAppWidgetOptionsChanged min width/height: $mWidth/$mHeight")

            updateAppWidget(context, appWidgetId)
            /*val updateConfigRequest: WorkRequest =
                OneTimeWorkRequestBuilder<UpdateEnabledWidgetConfigWorker>()
                    .build()

            WorkManager
                .getInstance(context)
                .enqueue(updateConfigRequest)*/
        }
    }

    fun updateAppWidget(
        context: Context,
        appWidgetId: Int,
        mIntent: Intent? = null,
        refresh: Boolean? = null
    ) {
        Log.d(TAG, "updateAppWidget called with appWidgetId $appWidgetId")
        /*val workData = workDataOf("APP_WIDGET_ID" to appWidgetId)
        val updateConfigRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<TravelPlannerWidgetUpdateWorker>()
                .setInputData(workData)
                .build()

        WorkManager
            .getInstance(context)
            .beginUniqueWork(
                "travel_planner_widget_update",
                ExistingWorkPolicy.REPLACE,
                updateConfigRequest
            )*/
        val bundle = PersistableBundle()
        bundle.putInt("APP_WIDGET_ID", appWidgetId)

        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1115, ComponentName(context ,TravelPlannerWidgetUpdateJobService::class.java))
        val job = jobInfo.setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setExtras(bundle)
            .build()

        jobScheduler.schedule(job)
    }
}