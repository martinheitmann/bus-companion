package com.app.skyss_companion.database

import androidx.room.*
import com.app.skyss_companion.dao.*
import com.app.skyss_companion.model.*
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan

@Database(
    entities = [
        StopGroup::class,
        Favorite::class,
        EnabledWidget::class,
        RecentlyUsed::class,
        BookmarkedRouteDirection::class,
        BookmarkedStopGroup::class,
        PassingTimeAlert::class,
        BookmarkedTravelPlan::class
   ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stopGroupDao(): StopGroupDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun enabledWidgetDao(): EnabledWidgetDao
    abstract fun recentlyUsedDao(): RecentlyUsedDao
    abstract fun bookmarkedRouteDirectionDao(): BookmarkedRouteDirectionDao
    abstract fun bookmarkedStopGroupDao(): BookmarkedStopGroupDao
    abstract fun bookmarkedTravelPlanDao(): BookmarkedTravelPlanDao
    abstract fun passingTimeAlertDao(): PassingTimeAlertDao
}