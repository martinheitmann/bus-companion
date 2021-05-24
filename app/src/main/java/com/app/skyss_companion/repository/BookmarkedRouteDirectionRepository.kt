package com.app.skyss_companion.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.BookmarkedRouteDirectionDao
import com.app.skyss_companion.model.BookmarkedRouteDirection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkedRouteDirectionRepository @Inject constructor(val bookmarkedRouteDirectionDao: BookmarkedRouteDirectionDao){
    @WorkerThread
    suspend fun getAllBookmarkedRouteDirections() : Flow<List<BookmarkedRouteDirection>> {
        Log.d("BRouteDirectionRepo", "getAllBookmarkedRouteDirections called.")
        return bookmarkedRouteDirectionDao.getAll()
    }

    @WorkerThread
    suspend fun getBookmarkedRouteDirection(bookmarkedStopGroupIdentifier: String, bookmarkedRouteDirectionIdentifier: String) : BookmarkedRouteDirection? {
        return bookmarkedRouteDirectionDao.getOne(bookmarkedStopGroupIdentifier, bookmarkedRouteDirectionIdentifier)
    }

    @WorkerThread
    suspend fun insertBookmarkedRouteDirections(bookmarkedRouteDirection: BookmarkedRouteDirection) {
        return bookmarkedRouteDirectionDao.insert(bookmarkedRouteDirection)
    }

    @WorkerThread
    suspend fun removeBookmarkedRouteDirection(bookmarkedRouteDirection: BookmarkedRouteDirection){
        return bookmarkedRouteDirectionDao.delete(bookmarkedRouteDirection)
    }


    @WorkerThread
    suspend fun removeBookmarkedRouteDirection(bookmarkedStopGroupIdentifier: String, bookmarkedRouteDirectionIdentifier: String){
        return bookmarkedRouteDirectionDao.delete(bookmarkedStopGroupIdentifier, bookmarkedRouteDirectionIdentifier)
    }

    @WorkerThread
    suspend fun bookmarkedRouteDirectionExists(bookmarkedStopGroupIdentifier: String, bookmarkedRouteDirectionIdentifier: String) : Flow<Boolean> {
        return bookmarkedRouteDirectionDao.exists(bookmarkedStopGroupIdentifier, bookmarkedRouteDirectionIdentifier)
    }
}