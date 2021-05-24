package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.BookmarkedStopGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedStopGroupDao {
    @Query("SELECT * FROM bookmarkedstopgroup")
    fun getAll(): Flow<List<BookmarkedStopGroup>>

    @Query("SELECT * FROM bookmarkedstopgroup WHERE stopGroupIdentifier = :stopGroupIdentifier")
    fun getOne(stopGroupIdentifier: String): BookmarkedStopGroup?

    @Query("SELECT EXISTS(SELECT * FROM bookmarkedstopgroup WHERE stopGroupIdentifier = :stopGroupIdentifier)")
    fun exists(stopGroupIdentifier: String) : Flow<Boolean>

    @Query("DELETE FROM bookmarkedstopgroup WHERE stopGroupIdentifier = :stopGroupIdentifier")
    fun delete(stopGroupIdentifier: String)

    @Insert
    fun insert(bookmarkedStopGroup: BookmarkedStopGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(bookmarkedStopGroup: List<BookmarkedStopGroup>)

    @Delete
    fun delete(bookmarkedStopGroup: BookmarkedStopGroup)
}