package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.RecentlyUsed
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyUsedDao {
    @Query("SELECT * FROM recentlyused")
    fun getAll(): Flow<List<RecentlyUsed>>

    @Query("SELECT * FROM recentlyused ORDER BY timestamp DESC")
    fun getAllByDate(): Flow<List<RecentlyUsed>>

    @Query("SELECT EXISTS(SELECT * FROM recentlyused WHERE identifier = :identifier)")
    fun exists(identifier: String): Flow<Boolean>

    @Query("DELETE FROM recentlyused WHERE identifier = :identifier")
    fun delete(identifier: String)

    @Query("DELETE FROM recentlyused WHERE identifier = (SELECT identifier FROM recentlyused ORDER BY timestamp ASC LIMIT 1)")
    fun deleteMostRecent()

    @Insert
    fun insert(stopGroup: RecentlyUsed)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stopGroup: List<RecentlyUsed>)

    @Delete
    fun delete(stopGroup: RecentlyUsed)
}