package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedTravelPlanDao {
    @Query("SELECT * FROM bookmarkedtravelplan")
    fun getAll(): Flow<List<BookmarkedTravelPlan>>

    @Query("SELECT * FROM bookmarkedtravelplan WHERE id = :id")
    fun getOne(id: Long): BookmarkedTravelPlan?

    @Query("SELECT EXISTS(SELECT * FROM bookmarkedtravelplan WHERE id = :id)")
    fun exists(id: Long) : Flow<Boolean>

    @Query("DELETE FROM bookmarkedtravelplan WHERE id = :id")
    fun delete(id: Long)

    @Insert
    fun insert(bookmarkedTravelPlan: BookmarkedTravelPlan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(bookmarkedTravelPlans: List<BookmarkedTravelPlan>)

    @Delete
    fun delete(bookmarkedTravelPlan: BookmarkedTravelPlan)
}