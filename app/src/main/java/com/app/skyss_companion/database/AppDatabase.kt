package com.app.skyss_companion.database

import androidx.room.*
import com.app.skyss_companion.dao.EnabledWidgetDao
import com.app.skyss_companion.dao.FavoriteDao
import com.app.skyss_companion.dao.StopGroupDao
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.Favorite
import com.app.skyss_companion.model.StopGroup

@Database(entities = [StopGroup::class, Favorite::class, EnabledWidget::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stopGroupDao(): StopGroupDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun enabledWidgetDao(): EnabledWidgetDao

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