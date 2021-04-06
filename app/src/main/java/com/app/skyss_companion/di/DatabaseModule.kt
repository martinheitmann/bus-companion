package com.app.skyss_companion.di

import android.content.Context
import androidx.room.Room
import com.app.skyss_companion.dao.EnabledWidgetDao
import com.app.skyss_companion.dao.FavoriteDao
import com.app.skyss_companion.dao.RecentlyUsedDao
import com.app.skyss_companion.dao.StopGroupDao
import com.app.skyss_companion.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideStopGroupDao(appDatabase: AppDatabase): StopGroupDao {
        return appDatabase.stopGroupDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Singleton
    @Provides
    fun provideRecentlyUsedDao(appDatabase: AppDatabase): RecentlyUsedDao {
        return appDatabase.recentlyUsedDao()
    }

    @Singleton
    @Provides
    fun provideEnabledWidgetDao(appDatabase: AppDatabase): EnabledWidgetDao {
        return appDatabase.enabledWidgetDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}