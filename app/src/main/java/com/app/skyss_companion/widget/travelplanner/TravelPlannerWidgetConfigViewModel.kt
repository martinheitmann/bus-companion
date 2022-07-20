package com.app.skyss_companion.widget.travelplanner

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.model.WidgetType
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import com.app.skyss_companion.repository.BookmarkedStopGroupRepository
import com.app.skyss_companion.repository.BookmarkedTravelPlanRepository
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelPlannerWidgetConfigViewModel @Inject constructor(
    application: Application,
    private val bookmarkedTravelPlanRepository: BookmarkedTravelPlanRepository,
    private val enabledWidgetRepository: EnabledWidgetRepository,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    val TAG = "TravPlanWidgetConfigVM"
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val savedTravelPlans: MutableLiveData<List<BookmarkedTravelPlan>> = MutableLiveData()
    var coroutineJob: Job? = null

    init {
        try {
            isLoading.postValue(true)
            coroutineJob = viewModelScope.launch(Dispatchers.IO) {
                bookmarkedTravelPlanRepository.getAllBookmarkedTravelPlans().collect { res ->
                    savedTravelPlans.postValue(res)
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
        position: Int,
        context: Context,
        onSuccess: (Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val element = savedTravelPlans.value?.get(position)
            Log.d(TAG, "Persisting widget with id: $widgetId")
            if (widgetId != 0 && element != null) {
                val ew = EnabledWidget(
                    widgetId = widgetId,
                    widgetType = WidgetType.TRAVEL_PLANNER,
                    fromFeature = element.fromFeature,
                    toFeature = element.toFeature,
                    timeType = element.timeType,
                    timestamp = element.timestamp,
                    modes = element.modes,
                    minimumTransferTime = element.minimumTransferTime,
                    maximumWalkDistance = element.maximumWalkDistance,
                )
                enabledWidgetRepository.insertEnabledWidget(ew)
                onSuccess(widgetId)
            } else {
                Toast.makeText(context, "Kunne ikke opprette widget...", Toast.LENGTH_SHORT)
            }
        }
    }

}