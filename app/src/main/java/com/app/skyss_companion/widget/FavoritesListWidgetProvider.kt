package com.app.skyss_companion.widget

import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import com.app.skyss_companion.workers.WidgetUpdateWorker

/**
 * Implementation of App Widget functionality.
 */
class FavoritesListWidgetProvider : AppWidgetProvider() {
    val TAG = "FavoritesLWProvider"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called for widgets with ids: $appWidgetIds")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetId)
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
        if(appWidgetIds != null && context != null) {
            val data = workDataOf("appWidgetIds" to appWidgetIds)
            val clearWidgetsRequest: WorkRequest = OneTimeWorkRequestBuilder<WidgetUpdateWorker>()
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

        val widgetOptions = appWidgetManager?.getAppWidgetOptions(appWidgetId)
        val width = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
        val height = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)
        val mWidth = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val mHeight = widgetOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        Log.d(TAG, "onAppWidgetOptionsChanged max width/height: $width/$height")
        Log.d(TAG, "onAppWidgetOptionsChanged min width/height: $mWidth/$mHeight")

    }

    fun updateAppWidget(
        context: Context,
        appWidgetId: Int,
    ) {
        Log.d(TAG, "updateAppWidget called with appWidgetId $appWidgetId")
        val intent = Intent()
        intent.putExtra("APP_WIDGET_ID", appWidgetId)
        JobIntentService.enqueueWork(context, FavoriteWidgetUpdateService::class.java, 0, intent)
    }
}