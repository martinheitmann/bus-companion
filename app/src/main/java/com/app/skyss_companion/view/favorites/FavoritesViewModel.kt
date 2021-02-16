package com.app.skyss_companion.view.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.FavoriteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    application: Application,
    private val favoriteRepository: FavoriteRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    val favoritedStopGroups: MutableLiveData<List<StopGroup>> = MutableLiveData()
    var coroutineJob: Job? = null

    init {
        coroutineJob = viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.getFavorites().collect { res ->
                val fetchedFavoritedStopGroups = favoriteRepository.getFavoritedStopGroups(
                    res.map { _res -> _res.identifier }
                )
                favoritedStopGroups.postValue(fetchedFavoritedStopGroups)
            }
        }
    }

}