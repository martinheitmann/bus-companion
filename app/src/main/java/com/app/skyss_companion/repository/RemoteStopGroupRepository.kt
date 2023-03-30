package com.app.skyss_companion.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.app.skyss_companion.http.StopsClient
import com.app.skyss_companion.mappers.StopResponseEntityMapper
import com.app.skyss_companion.model.StopGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteStopGroupRepository @Inject constructor(private val stopsClient: StopsClient) {
    val tag = "RemoteStopGroupRepository"

    private val _stopGroup = MutableStateFlow<StopGroup?>(null)
    val stopGroup = _stopGroup.asStateFlow()

    @WorkerThread
    private suspend fun fetchStopGroup(stopIdentifier: String): StopGroup? {
        if (stopIdentifier.isEmpty()) throw IllegalArgumentException("Argument 'stopIdentifier' must be a string of valid length.")
        stopsClient.fetchStopGroup(stopIdentifier)?.let { stopGroup ->
            return stopGroup
        } ?: run {
            return null
        }
    }

    suspend fun updateStopGroup(identifier: String){
        val stopGroup = fetchStopGroup(identifier)
        _stopGroup.update { stopGroup }
    }

}