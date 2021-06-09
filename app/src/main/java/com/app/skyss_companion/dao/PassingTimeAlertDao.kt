package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.PassingTimeAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface PassingTimeAlertDao {
    @Query("SELECT * FROM passingtimealert")
    fun getAll(): Flow<List<PassingTimeAlert>>

    @Query("SELECT EXISTS(SELECT * FROM passingtimealert WHERE id = :id)")
    fun exists(id: Int): Flow<Boolean>

    @Query("DELETE FROM passingtimealert WHERE id = :id")
    fun delete(id: Int)

    @Insert
    fun insert(passingTimeAlert: PassingTimeAlert)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(passingTimeAlert: List<PassingTimeAlert>)

    @Delete
    fun delete(passingTimeAlert: PassingTimeAlert)
}