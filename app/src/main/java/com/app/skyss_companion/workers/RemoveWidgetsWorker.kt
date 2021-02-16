package com.app.skyss_companion.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.skyss_companion.repository.EnabledWidgetRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RemoveWidgetsWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val enabledWidgetRepository: EnabledWidgetRepository
) : CoroutineWorker(appContext, workerParams) {
    val TAG = "RemoveWidgetsWorker"

    override suspend fun doWork(): Result {
        try {
            Log.d(TAG, "Worker started")
            val widgetIds = inputData.getIntArray("appWidgetIds")
            if(widgetIds != null){
                for(id in widgetIds){
                    enabledWidgetRepository.removeEnabledWidget(id)
                }
            }
            return Result.success()
        } catch (e: Throwable){
            Log.d(TAG, e.stackTraceToString())
            return Result.failure()
        }
    }

}