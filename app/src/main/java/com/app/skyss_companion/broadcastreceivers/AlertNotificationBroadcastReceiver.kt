package com.app.skyss_companion.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.misc.NotificationID
import com.app.skyss_companion.misc.NotificationUtils
import com.app.skyss_companion.repository.PassingTimeAlertRepository
import com.app.skyss_companion.repository.TimeTableRepository
import com.app.skyss_companion.view.routedirection_timetable.DateTimeTable
import com.app.skyss_companion.view.routedirection_timetable.PassingTimeDayTab
import com.app.skyss_companion.view.routedirection_timetable.PassingTimeListItem
import com.app.skyss_companion.workers.DeletePassingTimeAlertWorker
import com.app.skyss_companion.workers.RemoveWidgetsWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

/**
 * Class responsible for receiving the broadcasts sent by the AlarmManager
 * when triggered.
 */
@AndroidEntryPoint
class AlertNotificationBroadcastReceiver : BroadcastReceiver() {

    val TAG = "AlertNotifBroadcastReceiver"

    @Inject
    lateinit var timeTableRepository: TimeTableRepository

    @Inject
    lateinit var passingTimeAlertRepository: PassingTimeAlertRepository

    private val broadcastReceiverJob = Job()
    private val broadcastReceiverScope = CoroutineScope(Dispatchers.Main + broadcastReceiverJob)

    override fun onReceive(context: Context?, intent: Intent?) {
        // Intent data
        val passingTimeAlertId = intent?.extras?.get("passingTimeAlertId") as Long?
        val lineNumber = intent?.extras?.get("lineNumber") as String?
        val stopName = intent?.extras?.get("stopName") as String?
        val tripIdentifier = intent?.extras?.get("tripIdentifier") as String?
        val stopIdentifier = intent?.extras?.get("stopIdentifier") as String?
        val routeDirectionIdentifier = intent?.extras?.get("routeDirectionIdentifier") as String?
        val departureHour = intent?.extras?.get("departureHour") as Int?
        val departureMinute = intent?.extras?.get("departureMinute") as Int?
        val directionName = intent?.extras?.get("directionName") as String?

        // Constants
        val numberOfDays = 2

        if (stopIdentifier != null
            && passingTimeAlertId != null
            && routeDirectionIdentifier != null
            && departureMinute != null
            && departureHour != null
            && tripIdentifier != null
            && stopName != null
            && lineNumber != null
            && context != null
            && directionName != null
        ) {
            // We need to do async work in this receiver.
            val pendingResult = goAsync()
            Log.d(
                TAG,
                "onReceive values: $stopIdentifier, $routeDirectionIdentifier, $departureHour, $departureMinute"
            )
            broadcastReceiverScope.launch(Dispatchers.IO) {
                try {
                    withTimeout(9000) {
                        // If we can't perform the operation in 9 seconds,
                        // we're not interested in continuing anyways.
                        val timeTables = timeTableRepository.fetchTimeTables(
                            stopIdentifier,
                            routeDirectionIdentifier,
                            numberOfDays
                        )
                        val passingTimes = getTimeTablesPassingTimes(timeTables)
                        val passingTime = findPassingTimeByTripId(tripIdentifier, passingTimes)
                        val displayTime = passingTime?.displayTime
                        if (displayTime != null) {
                            //val notificationIntent = NotificationUtils.createAlertNotificationIntent(context)
                            val notificationBundle =
                                NotificationUtils.createAlertNotificationClickActionBundle(
                                    stopIdentifier = stopIdentifier,
                                    routeDirectionIdentifier = routeDirectionIdentifier,
                                    stopName = stopName,
                                    directionName = directionName,
                                    lineNumber = lineNumber
                                )
                            val notificationPendingIntent =
                                NotificationUtils.createNotificationClickActionDeepLinkPendingIntent(
                                    context, notificationBundle
                                )

                            val notificationText =
                                NotificationUtils.getAlertNotificationTextPreformatted(
                                    stopName = stopName,
                                    lineNumber = lineNumber,
                                    lineName = directionName,
                                    date = displayTime
                                )
                            val notificationTitle = NotificationUtils.getAlertNotificationTitle(
                                stopName = stopName,
                                lineNumber = lineNumber,
                            )
                            NotificationUtils.createNotification(
                                context,
                                context.getString(R.string.channel_id),
                                notificationTitle,
                                notificationText,
                                NotificationCompat.PRIORITY_MAX,
                                notificationPendingIntent,
                                NotificationID.id
                            )
                            scheduleRemovePassingTimeAlert(context, passingTimeAlertId)
                        }

                    }
                } catch (timeout: TimeoutCancellationException) {
                    // If the operation didn't finish quickly enough,
                    // just show the regular notification.
                    //val notificationIntent = NotificationUtils.createAlertNotificationIntent(context)
                    val notificationBundle =
                        NotificationUtils.createAlertNotificationClickActionBundle(
                            stopIdentifier = stopIdentifier,
                            routeDirectionIdentifier = routeDirectionIdentifier,
                            stopName = stopName,
                            directionName = directionName,
                            lineNumber = lineNumber
                        )
                    val notificationPendingIntent =
                        NotificationUtils.createNotificationClickActionDeepLinkPendingIntent(
                            context, notificationBundle
                        )

                    val notificationText =
                        NotificationUtils.getAlertNotificationTextPreformatted(
                            stopName = stopName,
                            lineNumber = lineNumber,
                            lineName = directionName,
                            date = "$departureHour:$departureMinute"
                        )
                    val notificationTitle = NotificationUtils.getAlertNotificationTitle(
                        stopName = stopName,
                        lineNumber = lineNumber,
                    )
                    NotificationUtils.createNotification(
                        context,
                        context.getString(R.string.channel_id),
                        notificationTitle,
                        notificationText,
                        NotificationCompat.PRIORITY_MAX,
                        notificationPendingIntent,
                        NotificationID.id
                    )
                    scheduleRemovePassingTimeAlert(context, passingTimeAlertId)
                } catch (e: Throwable) {
                    // ... and catch everything else.
                    Log.d(TAG, e.stackTraceToString())
                } finally {
                    // This block is important.
                    pendingResult.finish()
                }
            }
        }
    }

    /**
     * Instantiates a worker for removing the corresponding PassingTimeAlert entity
     * upon successful trigger.
     */
    private fun scheduleRemovePassingTimeAlert(context: Context, passingTimeAlertId: Long) {
        val data =
            workDataOf(context.getString(R.string.passing_time_alert_id) to passingTimeAlertId)
        val deletePassingTimeAlertRequest: WorkRequest =
            OneTimeWorkRequestBuilder<DeletePassingTimeAlertWorker>()
                .setInputData(data)
                .build()

        WorkManager
            .getInstance(context)
            .enqueue(deletePassingTimeAlertRequest)
    }

    /**
     * Finds the passing time with the argument trip identifier
     * in the provided list of passing time list items.
     */
    private fun findPassingTimeByTripId(
        tripIdentifier: String,
        items: List<PassingTimeListItem>
    ): PassingTimeListItem? {
        return items.find { pt -> pt.tripIdentifier == tripIdentifier }
    }

    /**
     * Provides a list of passing time items from the argument date time table.
     * Iterates over each date time table and concatenates the final result.
     */
    private fun getTimeTablesPassingTimes(timeTables: List<DateTimeTable>): List<PassingTimeListItem> {
        var displayItems = emptyList<PassingTimeListItem>()
        timeTables.forEach { timeTable ->
            timeTable.timeTable.passingTimes?.forEach { passingTime ->
                val localDateTime =
                    DateUtils.formatDate(passingTime.timestamp, DateUtils.DATE_PATTERN)
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
}