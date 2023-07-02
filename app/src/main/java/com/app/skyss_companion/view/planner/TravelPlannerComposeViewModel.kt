package com.app.skyss_companion.view.planner

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.BookmarkedTravelPlanRepository
import com.app.skyss_companion.repository.GeocodingRepository
import com.app.skyss_companion.repository.TravelPlannerRepository
import com.app.skyss_companion.view.planner.data.FeatureType
import com.app.skyss_companion.view.planner.data.TravelPlannerTimeType
import com.app.skyss_companion.view.planner.data.TravelPlannerViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@HiltViewModel
class TravelPlannerComposeViewModel @Inject constructor(
    application: Application,
    private val geocodingRepository: GeocodingRepository,
    private val travelPlannerRepository: TravelPlannerRepository,
    private val bookmarkedTravelPlanRepository: BookmarkedTravelPlanRepository,
    private val sharedPrefs: AppSharedPrefs
) : AndroidViewModel(application) {
    val classTag = "VM-40261"

    private var _viewState: MutableStateFlow<TravelPlannerViewState> =
        MutableStateFlow(TravelPlannerViewState())
    private var _geocodingFeatures: MutableStateFlow<List<GeocodingFeature>> =
        MutableStateFlow(emptyList())
    private var _travelPlans: MutableStateFlow<List<TravelPlan>> = MutableStateFlow(emptyList())
    private var _isLoadingTravelPlans: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _isLoadingGeocode: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val viewState: StateFlow<TravelPlannerViewState> = _viewState
    val geocodingFeatures: StateFlow<List<GeocodingFeature>> = _geocodingFeatures
    val travelPlans: StateFlow<List<TravelPlan>> = _travelPlans
    val isLoadingTravelPlans: StateFlow<Boolean> = _isLoadingTravelPlans
    val isLoadingGeocode: StateFlow<Boolean> = _isLoadingGeocode


    private var geocodeJob: Job = Job()
    private var travelPlanJob: Job = Job()
    private var travelPlanListenerJob: Job = Job()
    private val geocodeScope = viewModelScope + geocodeJob
    private var travelPlanScope = viewModelScope + travelPlanJob
    private var travelPlanListenerScope = viewModelScope + travelPlanListenerJob

    init {
        travelPlanListenerScope.launch {
            viewState.collect { state ->
                if (state.selectedDepartureFeature != null && state.selectedDestinationFeature != null){
                    fetchTravelPlans()
                } else {
                    Log.d(
                        classTag,
                        "[init] destination or departure feature null, skipping fetch."
                    )
                }
            }
        }
    }

    val savedTravelPlans: MutableLiveData<List<BookmarkedTravelPlan>> = MutableLiveData()
    val lastUsedLocations: MutableLiveData<List<GeocodingFeature>> = MutableLiveData()

    val modes = listOf("bus", "expressbus", "airportbus", "train", "boat", "tram", "others")
    val minimumTransferTime = 120

    val maximumWalkDistance = 2000

    private fun runGeocode(text: String) {
        Log.d(classTag, "[runGeocode] attempting to geocode '${text}'.")
        cancelJobIfActive(geocodeScope)
        geocodeJob = viewModelScope.launch {
            suspendWithLoading(_isLoadingGeocode) {
                delay(1500)
                val result = geocodingRepository.autocompleteGeocodeLocationText(text)
                Log.d(
                    classTag,
                    "[runGeocode] geocode attempt returned ${result?.features?.size} results."
                )
                _geocodingFeatures.value = result?.features ?: emptyList()
            }
        }
    }

    private suspend fun suspendWithLoading(
        flag: MutableStateFlow<Boolean>,
        func: suspend () -> Unit
    ) {
        try {
            flag.value = true
            Log.d(classTag, "[suspendWithLoading] loading flag set to ${flag.value}.")
            func()
        } finally {
            flag.value = false
            Log.d(classTag, "[suspendWithLoading] loading flag reset to ${flag.value}.")
        }
    }

    fun clearDialogData() {
        Log.d(classTag, "[clearDialogData] clearing dialog data.")
        _geocodingFeatures.value = emptyList()
        _viewState.value = _viewState.value.copy(destinationSearchText = "")
        _viewState.value = _viewState.value.copy(departureSearchText = "")
    }

    fun setDestinationSearchText(t: String) {
        Log.d(classTag, "[setDestinationSearchText] setting destination search text to '${t}'.")
        _viewState.value = _viewState.value.copy(destinationSearchText = t)
        if (t.length >= 3) {
            Log.d(
                classTag,
                "[setDestinationSearchText] destination search text '${t}' qualifies for geocode."
            )
            runGeocode(_viewState.value.destinationSearchText)
        } else {
            Log.d(
                classTag,
                "[setDestinationSearchText] destination search text '${t}' is too short to qualify for geocode."
            )
        }
    }

    fun setDepartureSearchText(t: String) {
        Log.d(classTag, "[setDestinationSearchText] setting departure search text to '${t}'.")
        _viewState.value = _viewState.value.copy(departureSearchText = t)
        if (t.length >= 3) {
            Log.d(
                classTag,
                "[setDepartureSearchText] departure search text '${t}' qualifies for geocode."
            )
            runGeocode(_viewState.value.departureSearchText)
        } else {
            Log.d(
                classTag,
                "[setDestinationSearchText] departure search text '${t}' is too short to qualify for geocode."
            )
        }
    }

    fun setTimeType(type: TravelPlannerTimeType) {
        Log.d(classTag, "[setTimeType] planner time type set to ${type}.")
        _viewState.value = _viewState.value.copy(timeType = type)
    }

    fun setPlannerTime(hourOfDay: Int, minute: Int) {
        val ldt = _viewState.value.plannerTime
            .withHour(hourOfDay)
            .withMinute(minute)
        val newState = _viewState.value.copy(plannerTime = ldt)
        _viewState.value = newState
        Log.d(
            classTag,
            "[setPlannerTime] planner time (hours, minutes) type set to (${hourOfDay}, ${minute})."
        )
    }

    fun setPlannerDate(year: Int, month: Int, dayOfMonth: Int) {
        val ldt = _viewState.value.plannerTime
            .withYear(year)
            .withMonth(month)
            .withDayOfMonth(dayOfMonth)
        val newState = _viewState.value.copy(plannerTime = ldt)
        _viewState.value = newState
    }

    fun setFeature(type: FeatureType, feature: GeocodingFeature) {
        if (type == FeatureType.DEPARTURE) {
            _viewState.value = _viewState.value.copy(selectedDepartureFeature = feature)
        } else if (type == FeatureType.DESTINATION) {
            _viewState.value = _viewState.value.copy(selectedDestinationFeature = feature)
        }
    }

    fun fetchTravelPlans() {
        val departureFeature = viewState.value.selectedDepartureFeature
        val destinationFeature = viewState.value.selectedDestinationFeature
        val timeType = viewState.value.timeType
        val timestamp = zonedDateTimeToUtcString(viewState.value.plannerTime)
        val modes = modes
        val mtt = minimumTransferTime
        val mwd = maximumWalkDistance
        Log.d(
            classTag,
            "[fetchTravelPlans] fetching travel plans using timestamp ${timestamp}."
        )
        if (departureFeature != null && destinationFeature != null) {
            travelPlanScope.launch {
                travelPlannerRepository.getTravelPlans(
                    fromFeature = departureFeature,
                    toFeature = destinationFeature,
                    timeType = timeType.name,
                    timestamp = timestamp,
                    modes = modes,
                    mtt = mtt,
                    mwd = mwd
                )?.let { result ->
                    Log.d(
                        classTag,
                        "[fetchTravelPlans] returned ${result.travelPlans.size} travelplans."
                    )
                    _travelPlans.value = result.travelPlans
                }
            }
        } else {
            Log.d(
                classTag,
                "[fetchTravelPlans] destination or departure feature null, skipping fetch."
            )
        }

    }

    private fun zonedDateTimeToUtcString(z: ZonedDateTime): String {
        val nz = z.withZoneSameInstant(ZoneId.of("UTC"))
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return formatter.format(nz)
    }

    /**
     * Cancels the argument job.
     * @param the job to be cancelled.
     * @return true if the argument job was active and canceled. False otherwise.
     */
    private fun cancelJobIfActive(scope: CoroutineScope): Boolean {
        return try {
            if (scope.isActive)
                scope.cancel("only a single instance of job can be active")
            false
        } catch (e: Throwable) {
            if (e is CancellationException) true
            else throw e
        }
    }
}