package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.EnabledWidgetDao
import com.app.skyss_companion.model.EnabledWidget
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnabledWidgetRepository @Inject constructor(private val enabledWidgetDao: EnabledWidgetDao) {

    @WorkerThread
    suspend fun getEnabledWidgets() : Flow<List<EnabledWidget>> {
        return enabledWidgetDao.getAll()
    }

    @WorkerThread
    suspend fun getEnabledWidget(widgetId: Int) : EnabledWidget? {
        return enabledWidgetDao.getOne(widgetId)
    }

    @WorkerThread
    suspend fun routeDirectionExists(identifier: String) : Flow<Boolean> {
        return enabledWidgetDao.routeDirectionExists(identifier)
    }

    @WorkerThread
    suspend fun stopGroupExists(identifier: String) : Flow<Boolean> {
        return enabledWidgetDao.stopGroupExists(identifier)
    }

    @WorkerThread
    suspend fun insertEnabledWidget(enabledWidget: EnabledWidget) {
        return enabledWidgetDao.insert(enabledWidget)
    }

    @WorkerThread
    suspend fun removeEnabledWidget(enabledWidget: EnabledWidget){
        return enabledWidgetDao.delete(enabledWidget)
    }


    @WorkerThread
    suspend fun removeEnabledWidget(widgetId: Int){
        return enabledWidgetDao.delete(widgetId)
    }

}