package com.app.skyss_companion.workers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.app.skyss_companion.R
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopGroupRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime

@HiltWorker
class UpdateEnabledWidgetConfigWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val enabledWidgetRepository: EnabledWidgetRepository
) : CoroutineWorker(appContext, workerParams) {
    val TAG = "UpdateEnabledWidgetCW"

    override suspend fun doWork(): Result {
        return try {
            val widgetId = inputData.getInt("OPTION_APPWIDGET_ID", -1)
            AppWidgetManager.getInstance(appContext).notifyAppWidgetViewDataChanged(widgetId, R.id.widget_stopgroup_listview)
            Result.success()
        } catch (exception: Exception) {
            Log.d(TAG, exception.stackTraceToString())
            Result.failure()
        }
    }
}