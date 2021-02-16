package com.app.skyss_companion.repository

import androidx.annotation.WorkerThread
import com.app.skyss_companion.dao.FavoriteDao
import com.app.skyss_companion.dao.StopGroupDao
import com.app.skyss_companion.model.Favorite
import com.app.skyss_companion.model.StopGroup
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao, private val stopGroupDao: StopGroupDao) {

    @WorkerThread
    suspend fun getFavorites() : Flow<List<Favorite>> {
        return favoriteDao.getAll()
    }

    @WorkerThread
    suspend fun exists(identifier: String) : Flow<Boolean> {
        return favoriteDao.exists(identifier)
    }

    @WorkerThread
    suspend fun insertFavorite(favorite: Favorite) {
        return favoriteDao.insert(favorite)
    }

    @WorkerThread
    suspend fun removeFavorite(favorite: Favorite){
        return favoriteDao.delete(favorite)
    }

    @WorkerThread
    suspend fun removeFavorite(identifier: String){
        return favoriteDao.delete(identifier)
    }

    @WorkerThread
    suspend fun getFavoritedStopGroups(identifiers: List<String>): List<StopGroup> {
        val stopGroups = mutableListOf<StopGroup>()
        for(identifier in identifiers){
            val stopGroup = stopGroupDao.find(identifier)
            if (stopGroup != null) {
                stopGroups.add(stopGroup)
            }
        }
        return stopGroups
    }
}