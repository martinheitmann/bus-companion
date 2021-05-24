package com.app.skyss_companion.view.bookmark.routedirections

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.repository.BookmarkedRouteDirectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkedRouteDirectionsListViewModel @Inject constructor(
    application: Application,
    private val bookmarkedRouteDirectionRepository: BookmarkedRouteDirectionRepository,
) : AndroidViewModel(
    application
) {
    val bookmarkedRouteDirections: MutableLiveData<List<BookmarkedRouteDirection>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkedRouteDirectionRepository.getAllBookmarkedRouteDirections().collect { res ->
                Log.d("FavoritesViewModel", "Bookmarked RouteDirections fetched: ${res.size}")
                bookmarkedRouteDirections.postValue(res)
            }
        }
    }
}