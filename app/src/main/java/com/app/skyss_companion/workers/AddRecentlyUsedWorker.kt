package com.app.skyss_companion.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.skyss_companion.model.RecentlyUsed
import com.app.skyss_companion.repository.RecentlyUsedRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class AddRecentlyUsedWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    val recentlyUsedRepository: RecentlyUsedRepository,
) : CoroutineWorker(appContext, workerParams) {
    val TAG = "RemoveWidgetsWorker"

    override suspend fun doWork(): Result {
        try {
            val recentlyUsedIdentifier = inputData.getString("RECENTLY_USED")
            if(recentlyUsedIdentifier != null){
                val recentlyUsed = RecentlyUsed(
                    identifier = recentlyUsedIdentifier,
                    timestamp = Date()
                )
                recentlyUsedRepository.insertRecentlyUsed(recentlyUsed)
                return Result.success()
            }
            return Result.success()
        } catch (e: Throwable){
            Log.d(TAG, e.stackTraceToString())
            return Result.failure()
        }
    }

}