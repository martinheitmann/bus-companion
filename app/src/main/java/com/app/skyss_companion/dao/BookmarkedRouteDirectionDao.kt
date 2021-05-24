package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.BookmarkedRouteDirection
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedRouteDirectionDao {
    @Query("SELECT * FROM bookmarkedroutedirection")
    fun getAll(): Flow<List<BookmarkedRouteDirection>>

    @Query("SELECT * FROM bookmarkedroutedirection WHERE stopGroupIdentifier = :stopGroupIdentifier AND routeDirectionIdentifier = :routeDirectionIdentifier")
    fun getOne(stopGroupIdentifier: String, routeDirectionIdentifier: String): BookmarkedRouteDirection?

    @Query("SELECT EXISTS(SELECT * FROM bookmarkedroutedirection WHERE routeDirectionIdentifier = :routeDirectionIdentifier AND stopGroupIdentifier = :stopGroupIdentifier)")
    fun exists(stopGroupIdentifier: String, routeDirectionIdentifier: String) : Flow<Boolean>

    @Query("DELETE FROM bookmarkedroutedirection WHERE stopGroupIdentifier = :stopGroupIdentifier AND routeDirectionIdentifier = :routeDirectionIdentifier")
    fun delete(stopGroupIdentifier: String, routeDirectionIdentifier: String)

    @Query("DELETE FROM bookmarkedroutedirection WHERE routeDirectionIdentifier = :routeDirectionIdentifier")
    fun delete(routeDirectionIdentifier: String)

    @Insert
    fun insert(bookmarkedRouteDirection: BookmarkedRouteDirection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(bookmarkedRouteDirections: List<BookmarkedRouteDirection>)

    @Delete
    fun delete(bookmarkedRouteDirection: BookmarkedRouteDirection)
}