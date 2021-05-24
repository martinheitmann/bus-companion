package com.app.skyss_companion.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.app.skyss_companion.dao.StopGroupDao
import com.app.skyss_companion.http.StopsClient
import com.app.skyss_companion.mappers.StopResponseEntityMapper
import com.app.skyss_companion.model.StopGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StopGroupRepository @Inject constructor(private val stopGroupDao: StopGroupDao, private val stopsClient: StopsClient) {

    val tag = "StopGroupRepository"
    val isSyncing = MutableLiveData<Boolean>()
    val allStopGroups: Flow<List<StopGroup>> = stopGroupDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(stopGroup: StopGroup) {
        stopGroupDao.insert(stopGroup)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(stopGroup: List<StopGroup>){
        stopGroupDao.insertAll(stopGroup)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun findStopGroup(identifier: String) : StopGroup? {
        return stopGroupDao.find(identifier)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateLocalStopResponses(){
        val fetchedStopGroups = stopsClient.fetchStopGroups()
        Log.d(tag, "fetched ${fetchedStopGroups?.StopGroups?.size} stop responses from api")
        if(fetchedStopGroups != null && fetchedStopGroups.StopGroups.isNotEmpty()){
            val mappedStopGroups = StopResponseEntityMapper.mapAllStopGroupResponses(fetchedStopGroups.StopGroups)
            Log.d(tag, "mapped ${mappedStopGroups.size} stop responses from stopGroup")
            stopGroupDao.insertAll(mappedStopGroups.filterNotNull())
        }
    }

    @WorkerThread
    fun filterSearchResults(searchTerm: String) : Flow<List<StopGroup>> {
       return allStopGroups.map { stopGroupsList -> stopGroupsList.filter { stopGroup -> stopGroup.description?.contains(searchTerm, ignoreCase = true) ?: false } }
    }

    @WorkerThread
    suspend fun findStopGroupsByIdentifiers(identifiers: List<String>): List<StopGroup> {
        return stopGroupDao.find(identifiers)
    }

}