package com.app.skyss_companion.dao

import androidx.room.*
import com.app.skyss_companion.model.EnabledWidget
import kotlinx.coroutines.flow.Flow

@Dao
interface EnabledWidgetDao {
    @Query("SELECT * FROM enabledwidget")
    fun getAll(): Flow<List<EnabledWidget>>

    @Query("SELECT * FROM enabledwidget WHERE widgetId = :appWidgetId")
    fun getOne(appWidgetId: Int): EnabledWidget?

    @Query("SELECT EXISTS(SELECT * FROM enabledwidget WHERE stopGroupIdentifier = :identifier)")
    fun stopGroupExists(identifier: String) : Flow<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM enabledwidget WHERE routeDirectionIdentifier = :identifier)")
    fun routeDirectionExists(identifier: String) : Flow<Boolean>

    @Query("DELETE FROM enabledwidget WHERE stopGroupIdentifier = :identifier")
    fun deleteStopGroup(identifier: String)

    @Query("DELETE FROM enabledwidget WHERE routeDirectionIdentifier = :identifier")
    fun deleteRouteDirection(identifier: String)

    @Query("DELETE FROM enabledwidget WHERE widgetId = :widgetId")
    fun delete(widgetId: Int)

    @Insert
    fun insert(enabledWidget: EnabledWidget)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(enabledWidget: List<EnabledWidget>)

    @Update
    fun updateEnabledWidget(enabledWidget: EnabledWidget)

    @Delete
    fun delete(enabledWidget: EnabledWidget)
}