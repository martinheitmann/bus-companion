package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.BookmarkedTravelPlanDao
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkedTravelPlanRepository @Inject constructor(
    private val bookmarkedTravelPlanDao: BookmarkedTravelPlanDao,
) {
    @WorkerThread
    suspend fun getAllBookmarkedTravelPlans(): Flow<List<BookmarkedTravelPlan>> {
        return bookmarkedTravelPlanDao.getAll()
    }

    @WorkerThread
    suspend fun getBookmarkedTravelPlan(id: Long): BookmarkedTravelPlan? {
        return bookmarkedTravelPlanDao.getOne(id)
    }

    @WorkerThread
    suspend fun insertBookmarkedTravelPlan(bookmarkedTravelPlan: BookmarkedTravelPlan) {
        return bookmarkedTravelPlanDao.insert(bookmarkedTravelPlan)
    }

    @WorkerThread
    suspend fun removeBookmarkedTravelPlan(bookmarkedTravelPlan: BookmarkedTravelPlan) {
        return bookmarkedTravelPlanDao.delete(bookmarkedTravelPlan)
    }

    @WorkerThread
    suspend fun removeBookmarkedTravelPlan(id: Long) {
        return bookmarkedTravelPlanDao.delete(id)
    }

    @WorkerThread
    suspend fun bookmarkedTravelPlanExists(id: Long): Flow<Boolean> {
        return bookmarkedTravelPlanDao.exists(id)
    }
}