package com.app.skyss_companion.view.stop_place

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.app.skyss_companion.misc.StopPlaceUtils
import com.app.skyss_companion.model.BookmarkedStopGroup
import com.app.skyss_companion.model.Stop
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.BookmarkedStopGroupRepository
import com.app.skyss_companion.repository.FavoriteRepository
import com.app.skyss_companion.repository.StopPlaceRepository
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
    private val bookmarkedStopGroupRepository: BookmarkedStopGroupRepository
) : AndroidViewModel(application) {
    val TAG = "StopPlaceViewModel"
    val stopGroup: MutableLiveData<StopGroup> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isBookmarked: MutableLiveData<Boolean> = MutableLiveData()
    val lineCodeFilter: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val filteredStopPlaceListItems: MediatorLiveData<List<StopPlaceListItem>> = MediatorLiveData()
    val lineCodes = MutableLiveData<List<String>>()
    val description = MutableLiveData<String?>()

    // We register the MediatorLiveData with the selected line codes
    // and stop group data in order to provide filtering through
    // the different LiveData sources.
    init {
        // This transformation should trigger whenever a stop group is fetched.
        filteredStopPlaceListItems.addSource(stopGroup) { stopGroup ->
            lineCodes.postValue(stopGroup.lineCodes ?: emptyList())
            val stops = stopGroup.stops
            if (stops != null) {
                val stopPlaceListItems = StopPlaceUtils.createListData(stops)
                filteredStopPlaceListItems.postValue(stopPlaceListItems)
            }
        }
        filteredStopPlaceListItems.addSource(lineCodeFilter){ lineCodes ->
            val stops: List<Stop> = stopGroup.value?.stops ?: emptyList()
            val newItems = filterByLineCodes(stops, lineCodes)
            filteredStopPlaceListItems.postValue(newItems)
        }
    }

    fun addLineCodeToFilter(index: Int){
        val currentFilter = lineCodeFilter.value?.toMutableList() ?: return
        val currentLineCodes = lineCodes.value?.toMutableList() ?: return
        val itemToAdd = currentLineCodes[index]
        if(currentFilter.contains(itemToAdd)) return
        currentFilter.add(itemToAdd)
        lineCodeFilter.postValue(currentFilter)
    }

    fun removeLineCodeFromFilter(index: Int){
        val currentlySelectedLineCodes = lineCodeFilter.value?.toMutableList() ?: return
        currentlySelectedLineCodes.removeAt(index)
        lineCodeFilter.postValue(currentlySelectedLineCodes)
    }

    fun checkIsFavorited(identifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            /*favoriteRepository.exists(identifier).collect { result ->
                isFavorited.postValue(result)
            }*/
            bookmarkedStopGroupRepository.bookmarkedStopGroupExists(identifier).collect { result ->
                isBookmarked.postValue(result)
            }
        }
    }

    private fun filterByLineCodes(stops: List<Stop>, lineCodes: List<String>): List<StopPlaceListItem>{
        if(lineCodes.isEmpty()) {
            Log.d(TAG, "Line code filter was empty.")
            return StopPlaceUtils.createListData(stops)
        }
        return StopPlaceUtils.createFilteredListData(stops, lineCodes)
    }

    fun removeFavorited(identifier: String){
        /*viewModelScope.launch(Dispatchers.IO) {
            favoriteRepository.removeFavorite(identifier)
        }*/
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkedStopGroupRepository.removeBookmarkedStopGroup(identifier)
        }

    }

    fun addFavorited(identifier: String){
        /*viewModelScope.launch(Dispatchers.IO) {
            val favorite = Favorite(identifier)
            favoriteRepository.insertFavorite(favorite)
        }*/
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarkedStopGroup = BookmarkedStopGroup(identifier)
            bookmarkedStopGroupRepository.insertBookmarkedStopGroup(bookmarkedStopGroup)
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

    fun getLineCodes(stopGroup: StopGroup): List<String> {
        return stopGroup.lineCodes ?: emptyList()
    }

    /**
     * Helper method for forcing a LiveData refresh.
     */
    fun <T> MutableLiveData<T>.forceRefresh() {
        this.value = this.value
    }




}