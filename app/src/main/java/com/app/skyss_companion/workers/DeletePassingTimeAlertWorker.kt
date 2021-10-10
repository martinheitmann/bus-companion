package com.app.skyss_companion.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.skyss_companion.R
import com.app.skyss_companion.repository.PassingTimeAlertRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Simple worker for deleting PassingTimeAlert entities
 * in the local database.
 */
@HiltWorker
class DeletePassingTimeAlertWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val passingTimeAlertRepository: PassingTimeAlertRepository
) : CoroutineWorker(appContext, workerParams) {
    val TAG = "DeletePassingTimeAlertWorker"

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "worker start")
            val passingTimeAlertId =
                inputData.getLong(appContext.getString(R.string.passing_time_alert_id), -1L) as Long?
            Log.d(TAG, "worker received id: $passingTimeAlertId")
            if (passingTimeAlertId != null && passingTimeAlertId != -1L) {
                passingTimeAlertRepository.removePassingTimeAlert(passingTimeAlertId)
                Log.d(TAG, "worker success, removed ")
                Result.success()
            } else {
                Log.d(TAG, "worker failure: passingTimeAlert was null or invalid")
                Result.failure()
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.stackTraceToString())
            Result.failure()
        }
    }
}