package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.PassingTimeAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface PassingTimeAlertDao {
    @Query("SELECT * FROM passingtimealert")
    fun getAll(): Flow<List<PassingTimeAlert>>

    @Query("SELECT * FROM passingtimealert WHERE id = :id")
    fun find(id: Long): PassingTimeAlert

    @Query("SELECT EXISTS(SELECT * FROM passingtimealert WHERE id = :id)")
    fun exists(id: Long): Flow<Boolean>

    @Query("DELETE FROM passingtimealert WHERE id = :id")
    fun delete(id: Long)

    @Insert
    fun insert(passingTimeAlert: PassingTimeAlert): Long

    @Insert
    fun insertAll(passingTimeAlert: List<PassingTimeAlert>): List<Long>

    @Delete
    fun delete(passingTimeAlert: PassingTimeAlert): Int
}