package com.app.skyss_companion.view.routedirection_timetable

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.misc.AlarmUtils
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.misc.NotificationUtils
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.model.PassingTime
import com.app.skyss_companion.model.PassingTimeAlert
import com.app.skyss_companion.repository.BookmarkedRouteDirectionRepository
import com.app.skyss_companion.repository.PassingTimeAlertRepository
import com.app.skyss_companion.repository.TimeTableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeTableComposeViewModel @Inject constructor(
    application: Application,
    private val timeTableRepository: TimeTableRepository,
    private val bookmarkedRouteDirectionRepository: BookmarkedRouteDirectionRepository,
    private val passingTimeAlertRepository: PassingTimeAlertRepository
) : AndroidViewModel(application) {
    val tag = "RouteDirTTableViewModel"

    private val _timeTables: MutableStateFlow<List<DateTimeTable>> = MutableStateFlow(emptyList())
    val passingTimes: Flow<List<Pair<LocalDateTime, List<PassingTimeListItem>>>> =
        _timeTables.map { t -> toPassingTimeListItems(t) }
    val isLoadingTimetables: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun fetchTimeTables(stopIdentifier: String, routeDirectionIdentifier: String) {
        if (stopIdentifier.isNotEmpty() && routeDirectionIdentifier.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                isLoadingTimetables.update { true }
                try {
                    val timeTables = timeTableRepository.fetchTimeTables(
                        stopIdentifier,
                        routeDirectionIdentifier,
                        6
                    )
                    _timeTables.update { timeTables }
                } catch (err: Throwable) {
                    Log.d(tag, err.stackTraceToString())
                } finally {
                    isLoadingTimetables.update { false }
                }
            }
        }
    }

    private fun toPassingTimeListItems(timeTables: List<DateTimeTable>): List<Pair<LocalDateTime, List<PassingTimeListItem>>> {
        return timeTables
            .map { t ->
                val date = t.date
                val passingTimes = t.timeTable.passingTimes?.filter { pt ->
                    DateUtils.isAfterNow(
                        pt.timestamp,
                        DateUtils.DATE_PATTERN
                    )
                }?.map { pt ->
                    PassingTimeListItem(
                        tripIdentifier = pt.tripIdentifier ?: "",
                        displayTime = pt.displayTime ?: "",
                        timeStamp = DateUtils.formatDate(
                            pt.timestamp,
                            DateUtils.DATE_PATTERN
                        ),
                        isSelected = false
                    )
                }
                Pair(date, passingTimes ?: emptyList())
            }

    }
}