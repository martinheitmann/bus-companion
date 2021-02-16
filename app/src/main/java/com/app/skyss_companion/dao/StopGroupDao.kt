package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.StopGroup
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
interface StopGroupDao {

    @Query("SELECT * FROM stopgroup WHERE identifier = :identifier")
    fun find(identifier: String): StopGroup?

    @Query("SELECT * FROM stopgroup")
    fun getAll(): Flow<List<StopGroup>>

    @Insert
    fun insert(stopGroup: StopGroup)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stopGroup: List<StopGroup>)

    @Delete
    fun delete(stopGroup: StopGroup)
}