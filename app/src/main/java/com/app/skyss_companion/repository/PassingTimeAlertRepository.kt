package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.PassingTimeAlertDao
import com.app.skyss_companion.model.Favorite
import com.app.skyss_companion.model.PassingTimeAlert
import com.app.skyss_companion.model.StopGroup
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PassingTimeAlertRepository @Inject constructor(
    val passingTimeAlertDao: PassingTimeAlertDao
){

    @WorkerThread
    suspend fun getPassingTimeAlerts() : Flow<List<PassingTimeAlert>> {
        return passingTimeAlertDao.getAll()
    }

    @WorkerThread
    suspend fun exists(id: Int) : Flow<Boolean> {
        return passingTimeAlertDao.exists(id)
    }

    @WorkerThread
    suspend fun insertPassingTimeAlert(passingTimeAlert: PassingTimeAlert) {
        return passingTimeAlertDao.insert(passingTimeAlert)
    }

    @WorkerThread
    suspend fun removePassingTimeAlert(passingTimeAlert: PassingTimeAlert){
        return passingTimeAlertDao.delete(passingTimeAlert)
    }

    @WorkerThread
    suspend fun removePassingTimeAlert(id: Int){
        return passingTimeAlertDao.delete(id)
    }

}