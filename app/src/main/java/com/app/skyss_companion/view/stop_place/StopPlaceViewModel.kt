package com.app.skyss_companion.view.stop_place

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.app.skyss_companion.model.Favorite
import com.app.skyss_companion.model.Stop
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.FavoriteRepository
import com.app.skyss_companion.repository.StopPlaceRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class StopPlaceViewModel @Inject constructor(
    application: Application,
    private val stopPlaceRepository: StopPlaceRepository,
    private val favoriteRepository: FavoriteRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    val TAG = "StopPlaceViewModel"
    val stopGroup: MutableLiveData<StopGroup> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorited: MutableLiveData<Boolean> = MutableLiveData()
    //var currentDate = LocalDateTime.parse("2020-12-12T16:15:00.000Z")

    fun checkIsFavorited(identifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.exists(identifier).collect { result ->
                isFavorited.postValue(result)
            }
        }
    }

    fun removeFavorited(identifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.removeFavorite(identifier)
        }
    }

    fun addFavorited(identifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = Favorite(identifier)
            favoriteRepository.insertFavorite(favorite)
        }
    }

    fun fetchStopPlace(identifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.postValue(true)
                val fetchedStopGroup = stopPlaceRepository.fetchStopPlace(identifier)
                stopGroup.postValue(fetchedStopGroup)
            } catch (exception: Exception){
                Log.d(TAG, exception.stackTraceToString())
            } finally {
                isLoading.postValue(false)
            }
        }
    }




}