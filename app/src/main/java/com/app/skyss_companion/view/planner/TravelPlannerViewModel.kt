package com.app.skyss_companion.view.planner

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.repository.GeocodingRepository
import com.app.skyss_companion.repository.TravelPlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TravelPlannerViewModel @Inject constructor(
    application: Application,
    private val geocodingRepository: GeocodingRepository,
    private val travelPlannerRepository: TravelPlannerRepository
) : AndroidViewModel(application) {

    private val tag = "TravelPlannerVM"

    var features: MutableLiveData<List<GeocodingFeature>> = MutableLiveData()
    var fetchFeaturesError: MutableLiveData<String?> = MutableLiveData(null)
    var fetchFeaturesLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var travelPlans: MutableLiveData<List<TravelPlan>> =
        MutableLiveData()
    var travelPlansLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var fetchFeaturesJob: Job? = null

    private val fetchTravelPlanJob = Job()
    private val fetchTravelPlanScope = CoroutineScope(Dispatchers.Main + fetchTravelPlanJob)

    var selectedFromFeature: MutableLiveData<GeocodingFeature?> = MutableLiveData(null)
    var selectedToFeature: MutableLiveData<GeocodingFeature?> = MutableLiveData(null)
    var mergedFeatures: MediatorLiveData<GeocodingFeature> = MediatorLiveData()

    var selectedLocalDateTime: MutableLiveData<LocalDateTime> = MutableLiveData(LocalDateTime.now())
    var selectedTimeType: MutableLiveData<Boolean> = MutableLiveData(false)

    val modes = listOf("bus", "expressbus", "airportbus", "train", "boat", "tram", "others")
    val minimumTransferTime = 120
    val maximumWalkDistance = 2000

    init {
        Log.d(tag, "viewmodel intializing")
        mergedFeatures.addSource(selectedFromFeature) {
            Log.d(tag, "selectedFromFeature livedata trigger")
            fetchTravelPlan()
        }
        mergedFeatures.addSource(selectedToFeature) {
            Log.d(tag, "selectedFromFeature livedata trigger")
            fetchTravelPlan()
        }
    }

    fun setToFeature(feature: GeocodingFeature) {
        selectedToFeature.postValue(feature)
    }

    fun setFromFeature(feature: GeocodingFeature) {
        selectedFromFeature.postValue(feature)
    }

    fun geocodeAutocomplete(text: String) {
        Log.d(tag, "attempting to geocode autocomplete '${text}'")
        if (fetchFeaturesJob?.isActive == true) {
            fetchFeaturesJob?.cancel("Function called with active job, old job is irrelevant")
        }
        fetchFeaturesJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(tag, "starting coroutine, setting loading and error values")
                fetchFeaturesLoading.postValue(true)
                fetchFeaturesError.postValue(null)
                delay(1000)
                val result = geocodingRepository.autocompleteGeocodeLocationText(text)
                Log.d(tag, "geocode autocomplete returned ${result?.features?.size} values")
                features.postValue(result?.features)
            } catch (e: Throwable) {
                if (e is CancellationException) {
                    Log.d(tag, "coroutine was cancelled due to receiving new input")
                } else {
                    Log.d(tag, e.stackTraceToString())
                    fetchFeaturesError.postValue("An error occurred while loading search results")
                }
            } finally {
                Log.d(tag, "finished method, resetting loading status")
                fetchFeaturesLoading.postValue(false)
            }
        }
    }

    private fun fetchTravelPlan() {
        travelPlansLoading.postValue(true)
        Log.d(tag, "fetchTravelPlan invoked")
        fetchTravelPlanScope.launch(Dispatchers.IO) {
            try {
                travelPlansLoading.postValue(true)
                Log.d(tag, "fetchTravelPlan coroutine running")
                val from = selectedFromFeature.value
                val to = selectedToFeature.value
                val timeType = getTimeType(selectedTimeType.value!!)
                val timestamp = toTimestampString(selectedLocalDateTime.value!!)
                if (to != null && from != null) {
                    val travelPlannerRoot = travelPlannerRepository.getTravelPlans(
                        fromFeature = from,
                        toFeature = to,
                        timeType = timeType,
                        timestamp = timestamp,
                        modes = modes,
                        mtt = minimumTransferTime,
                        mwd = maximumWalkDistance
                    )
                    Log.d(tag, "Travel planner response: $travelPlannerRoot")
                    travelPlannerRoot?.let { root ->
                        travelPlans.postValue(root.travelPlans)
                    }
                } else {
                    Log.d(tag, "to or from was null, skipping fetch sept")
                }
            } catch (e: Throwable) {
                Log.d(tag, e.stackTraceToString())
            } finally {
                travelPlansLoading.postValue(false)
            }
        }
    }

    fun formatDate(dt: LocalDateTime): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm")
        return formatter.format(dt)
    }

    fun toTimestampString(ldt: LocalDateTime): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm.ss'Z'")
        return formatter.format(ldt)
    }

    fun flipSwitch() {
        this.selectedTimeType.postValue(!this.selectedTimeType.value!!)
    }

    fun getTimeType(value: Boolean): String {
        return if (value) "ARRIVAL"
        else "DEPARTURE"
    }
}