package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.BookmarkedStopGroupDao
import com.app.skyss_companion.dao.StopGroupDao
import com.app.skyss_companion.model.BookmarkedStopGroup
import com.app.skyss_companion.model.StopGroup
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkedStopGroupRepository @Inject constructor(
    val bookmarkedStopGroupDao: BookmarkedStopGroupDao,
    val stopGroupDao: StopGroupDao
) {
    @WorkerThread
    suspend fun getAllBookmarkedStopGroups(): Flow<List<BookmarkedStopGroup>> {
        return bookmarkedStopGroupDao.getAll()
    }

    @WorkerThread
    suspend fun getBookmarkedStopGroup(bookmarkedStopGroupIdentifier: String): BookmarkedStopGroup? {
        return bookmarkedStopGroupDao.getOne(bookmarkedStopGroupIdentifier)
    }

    @WorkerThread
    suspend fun insertBookmarkedStopGroup(bookmarkedStopGroup: BookmarkedStopGroup) {
        return bookmarkedStopGroupDao.insert(bookmarkedStopGroup)
    }

    @WorkerThread
    suspend fun removeBookmarkedStopGroup(bookmarkedStopGroup: BookmarkedStopGroup) {
        return bookmarkedStopGroupDao.delete(bookmarkedStopGroup)
    }

    @WorkerThread
    suspend fun removeBookmarkedStopGroup(bookmarkedStopGroupIdentifier: String) {
        return bookmarkedStopGroupDao.delete(bookmarkedStopGroupIdentifier)
    }

    @WorkerThread
    suspend fun bookmarkedStopGroupExists(bookmarkedStopGroupIdentifier: String): Flow<Boolean> {
        return bookmarkedStopGroupDao.exists(bookmarkedStopGroupIdentifier)
    }

    @WorkerThread
    suspend fun getBookmarkedStopGroups(identifiers: List<String>): List<StopGroup> {
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