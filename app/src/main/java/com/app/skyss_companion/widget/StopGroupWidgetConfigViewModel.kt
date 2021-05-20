package com.app.skyss_companion.widget

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.FavoriteRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopGroupWidgetConfigViewModel @Inject constructor(
    application: Application,
    private val favoriteRepository: FavoriteRepository,
    private val enabledWidgetRepository: EnabledWidgetRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    val TAG = "FavoritesWidgetConfigVM"
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val favoritedStopGroups: MutableLiveData<List<StopGroup>> = MutableLiveData()
    var coroutineJob: Job? = null

    init {
        try {
            isLoading.postValue(true)
            coroutineJob = viewModelScope.launch(Dispatchers.IO) {
                favoriteRepository.getFavorites().collect { res ->
                    val fetchedFavoritedStopGroups = favoriteRepository.getFavoritedStopGroups(
                        res.map { _res -> _res.identifier }
                    )
                    favoritedStopGroups.postValue(fetchedFavoritedStopGroups)
                }
            }
        } catch(e: Throwable){
            Log.d(TAG, e.stackTraceToString())
        } finally {
            isLoading.postValue(false)
        }
    }

    fun persistEnabledWidget(widgetId: Int, identifier: String, onSuccess: (Int) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Persisting widget with id: $widgetId")
            if(widgetId != 0 && identifier.isNotEmpty()){
                val ew = EnabledWidget(widgetId = widgetId, identifier = identifier)
                enabledWidgetRepository.insertEnabledWidget(ew)
                onSuccess(widgetId)
            }
        }
    }

}