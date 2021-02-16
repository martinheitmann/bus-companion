package com.app.skyss_companion.view.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.StopGroupRepository
import com.app.skyss_companion.workers.StopGroupSyncWorker
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchStopsViewModel @Inject constructor(
    application: Application,
    private val stopsGroupRepository: StopGroupRepository,
    private val sharedPrefs: AppSharedPrefs,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val context = application.applicationContext
    val TAG = "SearchStopsViewModel"
    var stopSearchResults: MutableLiveData<List<StopGroup>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var isSyncing: MutableLiveData<Boolean> = MutableLiveData(false)
    private var coroutineJob: Job? = null

    init {
        checkIfSyncNecessary()
        stopsGroupRepository.isSyncing.observeForever { value ->
            isSyncing.postValue(value)
        }
    }

    fun filterResults(searchTerm: String) {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.postValue(true)
                stopsGroupRepository.filterSearchResults(searchTerm).collect { res ->
                    Log.d(TAG, "Search filter returned ${res.size} elements")
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

    private fun checkIfSyncNecessary(){
        val syncDataRequest: WorkRequest =
            OneTimeWorkRequestBuilder<StopGroupSyncWorker>()
                .build()

        WorkManager.getInstance(context).enqueue(syncDataRequest)
    }

    fun cancelJob(){
        coroutineJob?.cancel()
    }

}