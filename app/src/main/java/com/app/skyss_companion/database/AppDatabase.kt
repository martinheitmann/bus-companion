package com.app.skyss_companion.database

import androidx.room.*
import com.app.skyss_companion.dao.*
import com.app.skyss_companion.model.*

@Database(entities = [StopGroup::class, Favorite::class, EnabledWidget::class, RecentlyUsed::class, BookmarkedRouteDirection::class, BookmarkedStopGroup::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stopGroupDao(): StopGroupDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun enabledWidgetDao(): EnabledWidgetDao
    abstract fun recentlyUsedDao(): RecentlyUsedDao
    abstract fun bookmarkedRouteDirectionDao(): BookmarkedRouteDirectionDao
    abstract fun bookmarkedStopGroupDao(): BookmarkedStopGroupDao

   /* companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabaseInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }*/
}