package com.app.skyss_companion.view.alerts

import android.app.Application
import android.app.Notification
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.skyss_companion.misc.AlarmUtils
import com.app.skyss_companion.misc.NotificationUtils
import com.app.skyss_companion.model.PassingTimeAlert
import com.app.skyss_companion.repository.PassingTimeAlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveAlertsViewModel @Inject constructor(
    application: Application,
    private val passingTimeAlertRepository: PassingTimeAlertRepository
) : AndroidViewModel(application) {

    val alerts: MutableLiveData<List<PassingTimeAlert>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            passingTimeAlertRepository.getPassingTimeAlerts().collect { passingTimeAlerts ->
                alerts.postValue(passingTimeAlerts)
            }
        }
    }

    /**
     * Deletes an alert. Removes the alarm from the AlarmManager and
     * deletes the entity from the local database.
     */
    fun deletePassingTimeAlert(passingTimeAlert: PassingTimeAlert){
        viewModelScope.launch(Dispatchers.IO) {
            removeAlarm(getApplication(), passingTimeAlert)
            passingTimeAlertRepository.removePassingTimeAlert(passingTimeAlert)
        }
    }

    /**
     * Removes an existing alarm. The PendingIntent must be identical
     * to the one passed to the AlarmManager.
     */
    private fun removeAlarm(context: Context, passingTimeAlert: PassingTimeAlert){
        val intent = NotificationUtils.createAlertNotificationIntent(
            context,
            passingTimeAlert.id!!,
            passingTimeAlert.tripIdentifier,
            passingTimeAlert.stopIdentifier,
            passingTimeAlert.routeDirectionIdentifier,
            passingTimeAlert.stopName,
            passingTimeAlert.lineNumber,
            passingTimeAlert.departureHour,
            passingTimeAlert.departureMinute,
            passingTimeAlert.directionName
        )
        AlarmUtils.removeAlarm(context, intent)
    }
}