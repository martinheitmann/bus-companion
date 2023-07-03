package com.app.skyss_companion.widget.xml.routedirection

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.WidgetType
import com.app.skyss_companion.repository.BookmarkedRouteDirectionRepository
import com.app.skyss_companion.repository.EnabledWidgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDirectionWidgetConfigViewModel @Inject constructor(
    application: Application,
    private val bookmarkedRouteDirectionRepository: BookmarkedRouteDirectionRepository,
    private val enabledWidgetRepository: EnabledWidgetRepository,
) : AndroidViewModel(application) {
    val TAG = "RDWidgetConfigVM"
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val bookmarkedRouteDirections: MutableLiveData<List<BookmarkedRouteDirection>> =
        MutableLiveData()
    var coroutineJob: Job? = null

    init {
        try {
            isLoading.postValue(true)
            coroutineJob = viewModelScope.launch(Dispatchers.IO) {
                bookmarkedRouteDirectionRepository.getAllBookmarkedRouteDirections()
                    .collect { res ->
                        bookmarkedRouteDirections.postValue(res)
                    }
            }
        } catch (e: Throwable) {
            Log.d(TAG, e.stackTraceToString())
        } finally {
            isLoading.postValue(false)
        }
    }

    fun persistEnabledWidget(
        widgetId: Int,
        stopGroupIdentifier: String,
        routeDirectionIdentifier: String,
        onSuccess: (Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Persisting widget with id: $widgetId")
            if (widgetId != 0 && stopGroupIdentifier.isNotEmpty() && routeDirectionIdentifier.isNotEmpty()) {
                val ew = EnabledWidget(
                    widgetId = widgetId,
                    widgetType = WidgetType.FULL_STOPGROUP,
                    stopGroupIdentifier = stopGroupIdentifier,
                    routeDirectionIdentifier = routeDirectionIdentifier
                )
                enabledWidgetRepository.insertEnabledWidget(ew)
                onSuccess(widgetId)
            }
        }
    }

}