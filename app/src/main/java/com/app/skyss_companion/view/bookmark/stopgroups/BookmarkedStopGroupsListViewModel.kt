package com.app.skyss_companion.view.bookmark.stopgroups

import android.app.Application
import androidx.lifecycle.*
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.repository.BookmarkedStopGroupRepository
import com.app.skyss_companion.repository.StopGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkedStopGroupsListViewModel @Inject constructor(
    application: Application,
    private val bookmarkedStopGroupRepository: BookmarkedStopGroupRepository,
    private val stopGroupRepository: StopGroupRepository,
) : AndroidViewModel(application) {

    val bookmarkedStopGroups: MutableLiveData<List<StopGroup>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkedStopGroupRepository.getAllBookmarkedStopGroups().collect { res ->
                val stopGroups = stopGroupRepository.findStopGroupsByIdentifiers(res.map { _res -> _res.stopGroupIdentifier })
                bookmarkedStopGroups.postValue(stopGroups)
            }
        }
    }
}