package com.app.skyss_companion.view.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.app.skyss_companion.model.RecentlyUsed
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.RecentlyUsedRepository
import com.app.skyss_companion.repository.StopGroupRepository
import com.app.skyss_companion.workers.StopGroupSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchStopsViewModel @Inject constructor(
    application: Application,
    private val stopsGroupRepository: StopGroupRepository,
    private val recentlyUsedRepository: RecentlyUsedRepository
) : AndroidViewModel(application) {

    val TAG = "SearchStopsViewModel"
    var stopSearchResults: MutableLiveData<List<StopGroup>> = MutableLiveData()
    var recentlyUsedStopGroups: Flow<List<RecentlyUsed>>
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSyncing: MutableLiveData<Boolean> = MutableLiveData(false)
    private var coroutineJob: Job? = null

    init {
        checkIfSyncNecessary()
        stopsGroupRepository.isSyncing.observeForever { value ->
            isSyncing.postValue(value)
        }
        recentlyUsedStopGroups = recentlyUsedRepository.allRecentlyUsed
    }

    fun filterResults(searchTerm: String) {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.postValue(true)
                stopsGroupRepository.filterSearchResults(searchTerm).collect { res ->
                    Log.d(TAG, "Search filter returned ${res.size} elements: ${res.toString()}")
                    stopSearchResults.postValue(res)
                }
            } catch(e: Exception){
                Log.d(TAG, e.stackTraceToString())
            } finally {
                Log.d(TAG, "filterResults - finally reached")
                isLoading.postValue(false)
            }
        }
    }

    fun addStopGroupToRecentlyUsed(){

    }

    private fun checkIfSyncNecessary(){
        val syncDataRequest: WorkRequest =
            OneTimeWorkRequestBuilder<StopGroupSyncWorker>()
                .build()

        WorkManager.getInstance(getApplication()).enqueue(syncDataRequest)
    }

    fun cancelJob(){
        coroutineJob?.cancel()
    }

}