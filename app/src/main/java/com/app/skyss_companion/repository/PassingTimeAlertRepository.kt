package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.PassingTimeAlertDao
import com.app.skyss_companion.model.PassingTimeAlert
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PassingTimeAlertRepository @Inject constructor(
    private val passingTimeAlertDao: PassingTimeAlertDao
) {

    @WorkerThread
    suspend fun getPassingTimeAlerts(): Flow<List<PassingTimeAlert>> {
        return passingTimeAlertDao.getAll()
    }

    @WorkerThread
    suspend fun findPassingTimeAlert(id: Long): PassingTimeAlert {
        return passingTimeAlertDao.find(id)
    }

    @WorkerThread
    suspend fun exists(id: Long): Flow<Boolean> {
        return passingTimeAlertDao.exists(id)
    }

    @WorkerThread
    suspend fun insertPassingTimeAlert(passingTimeAlert: PassingTimeAlert): Long {
        return passingTimeAlertDao.insert(passingTimeAlert)
    }

    @WorkerThread
    suspend fun removePassingTimeAlert(passingTimeAlert: PassingTimeAlert): Int {
        return passingTimeAlertDao.delete(passingTimeAlert)
    }

    @WorkerThread
    suspend fun removePassingTimeAlert(id: Long){
        return passingTimeAlertDao.delete(id)
    }

}