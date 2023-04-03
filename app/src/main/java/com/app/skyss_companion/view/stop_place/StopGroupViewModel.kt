package com.app.skyss_companion.view.stop_place

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.misc.StopPlaceUtils
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.RemoteStopGroupRepository
import com.app.skyss_companion.view.stop_place.model.StopGroupListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class StopGroupViewModel @Inject constructor(
    application: Application,
    private val remoteStopGroupRepository: RemoteStopGroupRepository
) :
    AndroidViewModel(application) {
    val tag = "StopGroupViewModel"

    val isLoadingStopGroup: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stopGroup: MutableStateFlow<StopGroup?> = MutableStateFlow(null)
    val stopGroupList: Flow<List<StopGroupListItem>> = stopGroup.map { s ->
        s?.stops?.let { StopPlaceUtils.createViewData(it, emptyList()) } ?: emptyList()
    }


    fun updateStopGroup(identifier: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoadingStopGroup.update { true }
                val fetchedStopGroup = remoteStopGroupRepository.fetchStopGroup(identifier)
                stopGroup.update { fetchedStopGroup }
            } catch (exception: Exception) {
                Log.d(tag, exception.stackTraceToString())
            } finally {
                isLoadingStopGroup.update { true }
            }
        }
    }
}