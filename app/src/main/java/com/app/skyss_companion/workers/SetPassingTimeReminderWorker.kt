package com.app.skyss_companion.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

class SetPassingTimeReminderWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    val TAG = "RemoveWidgetsWorker"

    override suspend fun doWork(): Result {
        try {

            return Result.success()
        } catch (e: Throwable){
            Log.d(TAG, e.stackTraceToString())
            return Result.failure()
        }
    }

}