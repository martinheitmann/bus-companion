package com.app.skyss_companion.widget.routedirection

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.model.PassingTime
import com.app.skyss_companion.model.TimeTable
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.*
import com.app.skyss_companion.widget.RouteDirectionAppWidgetProvider
import com.app.skyss_companion.widget.StopGroupAppWidgetProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class RouteDirectionWidgetUpdateService : JobIntentService() {
    private val TAG = "RouteDirUpdateService"
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    @Inject
    lateinit var timeTableRepository: TimeTableRepository

    @Inject
    lateinit var bookmarkedRouteDirectionRepository: BookmarkedRouteDirectionRepository

    @Inject
    lateinit var enabledWidgetRepository: EnabledWidgetRepository

    @Inject
    lateinit var sharedPrefs: AppSharedPrefs

    override fun onHandleWork(mIntent: Intent) {
        Log.d(TAG, "onHandleWork invoked.")
        serviceScope.launch(Dispatchers.IO) {
            val manager = AppWidgetManager.getInstance(applicationContext)
            val appWidgetId = mIntent.extras?.get("APP_WIDGET_ID") as Int
            Log.d(TAG, "Received widget ID $appWidgetId")
            val enabledWidget = enabledWidgetRepository.getEnabledWidget(appWidgetId)
            enabledWidget?.routeDirectionIdentifier?.let { routeDirectionIdentifier ->
                enabledWidget.stopGroupIdentifier?.let { stopGroupIdentifier ->
                    bookmarkedRouteDirectionRepository.getBookmarkedRouteDirection(
                        stopGroupIdentifier,
                        routeDirectionIdentifier
                    )?.let { bookmarkedRouteDirection ->
                        val sharedPrefsLimit = fetchCellNumber()
                        val currentDate: String = getAndFormatCurrentDate()
                        val displayDates = fetchTimeTableDates(
                            enabledWidget.stopGroupIdentifier,
                            enabledWidget.routeDirectionIdentifier,
                            sharedPrefsLimit
                        )
                        val intentSync = createSyncIntent(enabledWidget.widgetId)
                        val pendingSync = PendingIntent.getBroadcast(
                            applicationContext,
                            enabledWidget.widgetId, // IMPORTANT: Use a unique request code!
                            intentSync,
                            PendingIntent.FLAG_ONE_SHOT
                        )

                        // Instantiate the RemoteViews object for the app widget layout.
                        val rv = createRemoteViews(
                            dateString = currentDate,
                            lineCode = bookmarkedRouteDirection.lineCode,
                            stopGroupName = bookmarkedRouteDirection.stopGroupName,
                            routeDirectionName = bookmarkedRouteDirection.routeDirectionName,
                            pendingIntentSync = pendingSync,
                            displayTimes = displayDates ?: emptyList(),
                        )
                        // If this isn't called explicitly, the widget won't refresh row column count on manual refresh
                        manager.notifyAppWidgetViewDataChanged(
                            enabledWidget.widgetId,
                            R.id.widget_stopgroup_listview
                        )
                        manager.updateAppWidget(enabledWidget.widgetId, rv)
                    }
                }
            }

        }
    }

    /**
     * Creates a date string in order to show the last sync timestamp.
     */
    private fun getAndFormatCurrentDate(): String {
        val pattern = "dd.MM.yyyy HH:mm:ss"
        // SimpleDateFormat behaves differently on Oreo builds and above, check version
        val simpleDateFormat: SimpleDateFormat =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat(
                    pattern, applicationContext.resources.configuration.locales.get(
                        0
                    )
                )
            } else {
                SimpleDateFormat(
                    pattern,
                    applicationContext.resources.configuration.locale
                )
            }
        return simpleDateFormat.format(Date())
    }

    /**
     * Creates the RemoteViews to be consumed by the AppWidgetManager.
     */
    private fun createRemoteViews(
        dateString: String,
        lineCode: String,
        stopGroupName: String,
        routeDirectionName: String,
        displayTimes: List<String>,
        pendingIntentSync: PendingIntent
    ): RemoteViews {
        val fullDateString = "Sist oppdatert: $dateString"
        return RemoteViews(
            applicationContext.packageName,
            R.layout.widget_view_passingtimes
        ).apply {
            removeAllViews(R.id.widget_passing_times_time_container)
            setTextViewText(R.id.widget_passing_times_last_updated, fullDateString)
            setTextViewText(R.id.widget_passing_times_linecode, lineCode)
            setTextViewText(R.id.widget_passing_times_name, routeDirectionName)
            setTextViewText(R.id.widget_passing_times_stop_group_name, stopGroupName)
            setOnClickPendingIntent(R.id.widget_passing_times_button_refresh, pendingIntentSync)
            if (displayTimes.isEmpty()) {
                val text = "Ingen flere avganger i dag"
                val textView =
                    RemoteViews(applicationContext.packageName, R.layout.remoteview_textview)
                textView.setTextViewText(R.id.remoteview_element_textview, text)
                addView(R.id.widget_passing_times_time_container, textView)
            } else {
                displayTimes.forEach { displayTime ->
                    val textView =
                        RemoteViews(applicationContext.packageName, R.layout.remoteview_textview)
                    textView.setTextViewText(R.id.remoteview_element_textview, displayTime)
                    addView(R.id.widget_passing_times_time_container, textView)
                }
            }
        }
    }


    /**
     * Creates the intent needed for the sync/refresh button to function.
     */
    private fun createSyncIntent(appWidgetId: Int): Intent {
        val intentSync = Intent(applicationContext, RouteDirectionAppWidgetProvider::class.java)
        intentSync.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intentSync.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        return intentSync
    }

    private suspend fun fetchTimeTableDates(
        stopGroupIdentifier: String,
        routeDirectionIdentifier: String,
        limit: Int
    ): List<String>? {
        val timeTables =
            timeTableRepository.fetchTimeTables(stopGroupIdentifier, routeDirectionIdentifier, 0)
        if (timeTables.isNotEmpty()) {
            timeTables[0].let { dateTimeTable ->
                val filteredTimeTable = filterPassedDisplayTimes(dateTimeTable.timeTable, limit)
                return filteredTimeTable?.mapNotNull { passingTime ->
                    passingTime.displayTime
                }
            }
        }
        return null
    }

    private fun filterPassedDisplayTimes(tt: TimeTable, limit: Int): List<PassingTime>? {
        return tt.passingTimes?.filter { passingTime -> DateUtils.isAfterNow(passingTime.timestamp, DateUtils.DATE_PATTERN)
        }?.take(limit)
    }

    private suspend fun fetchCellNumber(): Int {
        try {
            val cellNumber: String? = sharedPrefs.readWidgetTimeItemsLimit()
            if(cellNumber != null) {
                return cellNumber.toInt()
            }
            return 3
        } catch (exception: Throwable){
            Log.d(TAG, exception.stackTraceToString())
            return 3
        }
    }
}