package com.app.skyss_companion.view.planner.selected_plan

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.repository.BookmarkedRouteDirectionRepository
import com.app.skyss_companion.repository.PassingTimeAlertRepository
import com.app.skyss_companion.repository.TimeTableRepository
import com.app.skyss_companion.repository.TravelPlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SelectedTravelPlanViewModel @Inject constructor(
    application: Application,
    private val travelPlannerRepository: TravelPlannerRepository
) : AndroidViewModel(application) {
    val tag = "SelectedTPViewModel"

    var selectedTravelPlan: MutableLiveData<TravelPlan> = MutableLiveData()
    var travelPlanLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun fetchTravelPlan(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(tag, "fetchTravelPlan called for id $id")
            val travelPlan = travelPlannerRepository.getTravelPlanById(id)
            travelPlan?.let { root ->
                val item = root.travelPlans.firstOrNull()
                item?.let { tp ->
                    selectedTravelPlan.postValue(tp)
                }
            }
            try {
                travelPlanLoading.postValue(true)
            } catch (e: Throwable) {
                Log.d(tag, e.stackTraceToString())
            } finally {
                travelPlanLoading.postValue(false)
            }
        }
    }

    fun getDateString(t: TravelPlan): String? {
        val start = t.startTime
        val end = t.endTime
        if (start != null && end != null) {
            val day = start.dayOfWeek
            val dayValue = start.dayOfMonth
            val month = start.month.value
            val year = start.year
            val startHour = start.toLocalTime().hour.toString().padStart(2, '0')
            val startMinute = start.toLocalTime().minute.toString().padStart(2, '0')
            val endHour = end.toLocalTime().hour.toString().padStart(2, '0')
            val endMinute = end.toLocalTime().minute.toString().padStart(2, '0')
            return "${
                day.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                )
            } $dayValue.$month.$year $startHour:$startMinute - $endHour:$endMinute"
        }
        return null
    }

    fun getDurationString(t: TravelPlan): String? {
        val start = t.startTime
        val end = t.endTime
        if(start != null && end != null){
            val duration = Duration.between(start, end)
            val minutes = duration.toMinutes().toInt()
            if(minutes == 1) return "1 minutt"
            return "(${minutes} minutter)"
        }
        return null
    }
}