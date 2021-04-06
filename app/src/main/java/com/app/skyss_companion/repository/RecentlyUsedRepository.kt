package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.RecentlyUsedDao
import com.app.skyss_companion.dao.StopGroupDao
import com.app.skyss_companion.model.RecentlyUsed
import com.app.skyss_companion.model.StopGroup
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentlyUsedRepository @Inject constructor(
    private val recentlyUsedDao: RecentlyUsedDao,
    private val stopGroupDao: StopGroupDao
) {

    private val MAX_ITEMS = 20
    var allRecentlyUsed: Flow<List<RecentlyUsed>> = recentlyUsedDao.getAllByDate()

    @WorkerThread
    suspend fun getAllRecentlyUsed(): Flow<List<RecentlyUsed>> {
        return allRecentlyUsed
    }

    @WorkerThread
    suspend fun exists(identifier: String): Boolean {
        var result = false
        allRecentlyUsed.collect { element ->
            val search = element.find { item -> item.identifier == identifier }
            if(search != null) result = true
        }
        return result
    }

    @WorkerThread
    suspend fun insertRecentlyUsed(recentlyUsed: RecentlyUsed) {
        val items = allRecentlyUsed.first()
        if(items.size < 20) return recentlyUsedDao.insert(recentlyUsed)
        recentlyUsedDao.deleteMostRecent()
        return recentlyUsedDao.insert(recentlyUsed)
    }

    @WorkerThread
    suspend fun removeRecentlyUsed(recentlyUsed: RecentlyUsed) {
        return recentlyUsedDao.delete(recentlyUsed)
    }

    @WorkerThread
    suspend fun removeRecentlyUsed(identifier: String) {
        return recentlyUsedDao.delete(identifier)
    }

    @WorkerThread
    suspend fun getRecentlyUsedStopGroups(identifiers: List<String>): List<StopGroup> {
        val stopGroups = mutableListOf<StopGroup>()
        for (identifier in identifiers) {
            val stopGroup = stopGroupDao.find(identifier)
            if (stopGroup != null) {
                stopGroups.add(stopGroup)
            }
        }
        return stopGroups
    }
}