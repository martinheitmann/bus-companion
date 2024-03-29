package com.app.skyss_companion.view.planner

import android.app.Application
import android.util.Log
import android.widget.Toast
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TravelPlannerViewModel @Inject constructor(
    application: Application,
    private val geocodingRepository: GeocodingRepository,
    private val travelPlannerRepository: TravelPlannerRepository,
    private val bookmarkedTravelPlanRepository: BookmarkedTravelPlanRepository,
    private val sharedPrefs: AppSharedPrefs
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
    private val fetchBookmarkedTravelPlansJob = Job()
    private val fetchTravelPlanScope = CoroutineScope(Dispatchers.Main + fetchTravelPlanJob)
    private val fetchBookmarkedTravelPlansScope =
        CoroutineScope(Dispatchers.Main + fetchBookmarkedTravelPlansJob)

    var selectedFromFeature: MutableLiveData<GeocodingFeature?> = MutableLiveData(null)
    var selectedToFeature: MutableLiveData<GeocodingFeature?> = MutableLiveData(null)
    var mergedFeatures: MediatorLiveData<GeocodingFeature> = MediatorLiveData()

    var selectedLocalDateTime: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now().withSecond(0))
    var selectedTimeType: MutableLiveData<Boolean> = MutableLiveData(false)

    val savedTravelPlans: MutableLiveData<List<BookmarkedTravelPlan>> = MutableLiveData()
    val lastUsedLocations: MutableLiveData<List<GeocodingFeature>> = MutableLiveData()

    val modes = listOf("bus", "expressbus", "airportbus", "train", "boat", "tram", "others")
    val minimumTransferTime = 120
    val maximumWalkDistance = 2000

    init {
        Log.d(tag, "viewmodel intializing")
        mergedFeatures.addSource(selectedFromFeature) {
            Log.d(tag, "selectedFromFeature livedata trigger")
            fetchTravelPlan()
        }
        mergedFeatures.addSource(selectedLocalDateTime) {
            val toFeature = selectedToFeature.value
            val fromFeature = selectedFromFeature.value
            if (toFeature != null && fromFeature != null) {
                Log.d(tag, "selectedLocalDateTime livedata trigger, fetching as result")
                fetchTravelPlan()
            } else Log.d(tag, "selectedLocalDateTime livedata trigger, fetch conditions not met")

        }
        mergedFeatures.addSource(selectedToFeature) {
            Log.d(tag, "selectedToFeature livedata trigger")
            fetchTravelPlan()
        }
        fetchBookmarkedTravelPlansScope.launch(Dispatchers.IO) {
            bookmarkedTravelPlanRepository.getAllBookmarkedTravelPlans().collect { travelPlans ->
                savedTravelPlans.postValue(travelPlans)
            }
        }
    }

    fun setToFeature(feature: GeocodingFeature) {
        selectedToFeature.postValue(feature)
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.writeLastUsedGeocodingFeatures(feature)
        }
    }

    fun setFromFeature(feature: GeocodingFeature) {
        selectedFromFeature.postValue(feature)
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.writeLastUsedGeocodingFeatures(feature)
        }
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

    fun cancelGeocode() {
        if (fetchFeaturesJob?.isActive == true) {
            fetchFeaturesJob?.cancel("Function called with active job, old job is irrelevant")
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

    private fun toTimestampString(ldt: LocalDateTime): String {
        val asZonedTime = ZonedDateTime.of(ldt, ZoneId.of("Europe/Oslo"))
        val asUtc = asZonedTime.withZoneSameInstant( ZoneId.of("UTC") )
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return formatter.format(asUtc)
    }

    fun flipSwitch() {
        this.selectedTimeType.postValue(!this.selectedTimeType.value!!)
    }

    private fun getTimeType(value: Boolean): String {
        return if (value) "ARRIVAL"
        else "DEPARTURE"
    }

    fun saveCurrentSearch() {
        val currentToFeature = selectedToFeature.value
        val currentFromFeature = selectedFromFeature.value
        val minimumTransferTime = minimumTransferTime
        val maximumWalkDistance = maximumWalkDistance
        val timeType = getTimeType(selectedTimeType.value!!)
        val timestamp = toTimestampString(selectedLocalDateTime.value!!)
        val modes = modes
        if (currentFromFeature != null && currentToFeature != null) {
            fetchBookmarkedTravelPlansScope.launch(Dispatchers.IO) {
                val saved = BookmarkedTravelPlan(
                    fromFeature = currentFromFeature,
                    toFeature = currentToFeature,
                    minimumTransferTime = minimumTransferTime,
                    maximumWalkDistance = maximumWalkDistance,
                    modes = modes,
                    timestamp = timestamp,
                    timeType = timeType,
                    createdAt = LocalDateTime.now()
                )
                bookmarkedTravelPlanRepository.insertBookmarkedTravelPlan(saved)
                ContextCompat.getMainExecutor(getApplication()).execute {
                    val text = "Søk lagret!"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(getApplication(), text, duration)
                    toast.show()
                }
            }
        } else {
            val text = "Både start og destinasjon må være definert for å lagre søk."
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(getApplication(), text, duration)
            toast.show()
        }
    }

    suspend fun getLastUsedLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "getLastUsedLocations starting coroutine with viewModel scope.")
            val ret: List<GeocodingFeature> =
                sharedPrefs.readLastUsedGeocodingFeatures() ?: emptyList()
            Log.d(tag, "getLastUsedLocations returned ${ret.size} items.")
            lastUsedLocations.postValue(ret)
        }
    }

    suspend fun addLastUsedLocation(geocodingFeature: GeocodingFeature) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPrefs.writeLastUsedGeocodingFeatures(geocodingFeature)
        }
    }

    fun deleteSavedTravelPlan(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            savedTravelPlans.value?.let { data ->
                data[position].let { element ->
                    bookmarkedTravelPlanRepository.removeBookmarkedTravelPlan(element)
                }
            }
        }
    }
}