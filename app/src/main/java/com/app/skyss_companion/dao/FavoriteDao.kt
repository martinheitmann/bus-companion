package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.Favorite
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<Favorite>>

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE identifier = :identifier)")
    fun exists(identifier: String) : Flow<Boolean>

    @Query("DELETE FROM favorite WHERE identifier = :identifier")
    fun delete(identifier: String)

    @Insert
    fun insert(stopGroup: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stopGroup: List<Favorite>)

    @Delete
    fun delete(stopGroup: Favorite)
}