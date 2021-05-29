package com.app.skyss_companion.view.routedirection_timetable

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.model.PassingTime
import com.app.skyss_companion.repository.BookmarkedRouteDirectionRepository
import com.app.skyss_companion.repository.TimeTableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RouteDirectionTimeTableViewModel @Inject constructor(
    application: Application,
    private val timeTableRepository: TimeTableRepository,
    private val bookmarkedRouteDirectionRepository: BookmarkedRouteDirectionRepository
) : AndroidViewModel(application) {
    val TAG = "RouteDirTTableViewModel"

    val dateTimeTables: MutableLiveData<List<DateTimeTable>> = MutableLiveData()
    val passingTimeDayTabs: MediatorLiveData<List<PassingTimeDayTab>> = MediatorLiveData()
    val passingTimeListItems: MediatorLiveData<List<PassingTimeListItem>> = MediatorLiveData()
    val selectedDayTab: MutableLiveData<PassingTimeDayTab?> = MutableLiveData()
    val isBookmarked: MutableLiveData<Boolean?> = MutableLiveData()

    init {
        passingTimeDayTabs.addSource(dateTimeTables) { dateTimeTables ->
            val days = getTimeTableFilterDisplays(dateTimeTables)
            passingTimeDayTabs.postValue(days)
        }
        passingTimeListItems.addSource(dateTimeTables) { dateTimeTables ->
            Log.d(TAG, "Converting DateTimeTable to list items...")
            val items = getTimeTablesPassingTimes(dateTimeTables)
            passingTimeListItems.postValue(items)
        }
        passingTimeListItems.addSource(selectedDayTab) { selectedDayTab ->
            Log.d(TAG, "Selected day triggered: $selectedDayTab")
            val items = getTimeTablesPassingTimes(dateTimeTables.value ?: emptyList())
            val filteredItems = applyFilter(selectedDayTab, items)
            passingTimeListItems.postValue(filteredItems)
        }

    }

    fun checkIsBookmarked(stopGroupIdentifier: String, routeDirectionIdentifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkedRouteDirectionRepository.bookmarkedRouteDirectionExists(stopGroupIdentifier, routeDirectionIdentifier).collect { result ->
                isBookmarked.postValue(result)
            }
        }
    }

    fun bookmark(routeDirectionIdentifier: String, stopGroupIdentifier: String, routeDirectionName: String, stopGroupName: String, lineCode: String){
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarkedRouteDirection = BookmarkedRouteDirection(
                routeDirectionIdentifier = routeDirectionIdentifier,
                stopGroupIdentifier = stopGroupIdentifier,
                lineCode = lineCode,
                routeDirectionName = routeDirectionName,
                stopGroupName = stopGroupName
            )
            bookmarkedRouteDirectionRepository.insertBookmarkedRouteDirections(bookmarkedRouteDirection)
        }
    }

    fun removeBookmark(stopGroupIdentifier: String, routeDirectionIdentifier: String){
        viewModelScope.launch(Dispatchers.IO) {
            bookmarkedRouteDirectionRepository.removeBookmarkedRouteDirection(stopGroupIdentifier, routeDirectionIdentifier)
        }
    }

    fun setSelectedDayTab(tabs: List<PassingTimeDayTab>){
        Log.d(TAG, "setSelectedDayTab received list: $tabs")
        tabs.find { t -> t.isSelected }?.let {
            Log.d(TAG, "setSelectedDayTab found item: $it")
            selectedDayTab.postValue(it)
        } ?: selectedDayTab.postValue(null)
    }

    fun fetchTimeTables(stopIdentifier: String, routeDirectionIdentifier: String) {
        if (stopIdentifier.isNotEmpty() && routeDirectionIdentifier.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                dateTimeTables.postValue(
                    timeTableRepository.fetchTimeTables(
                        stopIdentifier,
                        routeDirectionIdentifier,
                        6
                    )
                )
            }
        }
    }

    fun markSelected(index: Int) {
        val newList = passingTimeDayTabs.value?.mapIndexed { mIndex, passingTimeDayTab ->
            if(mIndex == index && !passingTimeDayTab.isSelected) passingTimeDayTab.copy(isSelected = true)
            else passingTimeDayTab.copy(isSelected = false)
        }
        passingTimeDayTabs.postValue(newList)
    }

    fun applyFilter(
        filter: PassingTimeDayTab?,
        items: List<PassingTimeListItem>
    ): List<PassingTimeListItem> {
        if (filter != null) {
            return items.filter { i ->
                i.timeStamp.dayOfMonth == filter.time.dayOfMonth
                        && i.timeStamp.year == filter.time.year
            }
        }
        return items
    }

    private fun getTimeTablesPassingTimes(timeTables: List<DateTimeTable>): List<PassingTimeListItem> {
        var displayItems = emptyList<PassingTimeListItem>()
        timeTables.forEach { timeTable ->
            Log.d(TAG, "Passing times before filtering: ${timeTable.timeTable.passingTimes}")
             val ftt = timeTable.timeTable.passingTimes
                ?.filter { passingTime -> DateUtils.isAfterNow(passingTime.timestamp, DateUtils.DATE_PATTERN) }
            Log.d(TAG, "Date being filtered after: ${LocalDateTime.now()}")
            Log.d(TAG, "Passing times after filtering: $ftt")
                ftt?.forEach { passingTime ->
                    val localDateTime = DateUtils.formatDate(passingTime.timestamp, DateUtils.DATE_PATTERN)
                    val displayItem = PassingTimeListItem(
                        tripIdentifier = passingTime.tripIdentifier ?: "",
                        displayTime = passingTime.displayTime ?: "",
                        timeStamp = localDateTime,
                        isSelected = false
                    )
                    displayItems = displayItems + displayItem
                }
        }
        return displayItems
    }

    fun getTimeTableFilterDisplays(timeTables: List<DateTimeTable>): List<PassingTimeDayTab> {
        var filterItems = emptyList<PassingTimeDayTab>()
        timeTables.forEach { timeTable ->
            val timeTableDate = timeTable.date
            val dayOfWeek =
                timeTableDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val filterItem = PassingTimeDayTab(
                time = timeTableDate,
                display = dayOfWeek,
                isSelected = false
            )
            filterItems = filterItems + filterItem
        }
        return filterItems
    }
}