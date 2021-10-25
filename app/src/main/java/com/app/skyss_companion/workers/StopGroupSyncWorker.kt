package com.app.skyss_companion.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.StopGroupRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime

@HiltWorker
class StopGroupSyncWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val stopGroupRepository: StopGroupRepository,
    private val sharedPrefs: AppSharedPrefs
) : CoroutineWorker(appContext, workerParams) {
    val TAG = "StopGroupSyncWorker"

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Running sync check task")
            val currentTime = LocalDateTime.now()
            val lastSynced = sharedPrefs.readLastSynced()
            if (lastSynced != null) {
                Log.d(TAG, "Checking last sync date")
                val timeBetween = Duration.between(lastSynced, currentTime)
                if (timeBetween.toHours() > 24) {
                    Log.d(TAG, "More than 24 hours since sync, performing sync")
                    stopGroupRepository.isSyncing.postValue(true)
                    stopGroupRepository.updateLocalStopResponses()
                    stopGroupRepository.isSyncing.postValue(false)
                    sharedPrefs.writeLastSynced(currentTime)
                } else {
                    Log.d(TAG, "Less than 24 hours since sync, skipping sync")
                }
            } else {
                Log.d(TAG, "Couldn't find last sync date, performing sync")
                stopGroupRepository.isSyncing.postValue(true)
                stopGroupRepository.updateLocalStopResponses()
                stopGroupRepository.isSyncing.postValue(false)
                sharedPrefs.writeLastSynced(currentTime)
            }
            stopGroupRepository.isSyncing.postValue(false)
            Result.success()
        } catch (exception: Exception) {
            Log.d(TAG, "worker caught exception: " + exception.stackTraceToString())
            stopGroupRepository.isSyncing.postValue(false)
            Result.failure()
        } finally {
            stopGroupRepository.isSyncing.postValue(false)
        }
    }
}