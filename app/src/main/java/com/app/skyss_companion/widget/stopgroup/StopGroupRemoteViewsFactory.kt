package com.app.skyss_companion.widget.stopgroup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.StopPlaceUtils
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.StopPlaceRepository
import com.app.skyss_companion.view.stop_place.StopPlaceListDivider
import com.app.skyss_companion.view.stop_place.StopPlaceListEntry
import com.app.skyss_companion.view.stop_place.StopPlaceListItem
import com.app.skyss_companion.widget.WidgetUtils
import kotlinx.coroutines.runBlocking
import java.io.IOException


class StopGroupRemoteViewsFactory(
    intent: Intent,
    val context: Context,
    val stopPlaceRepository: StopPlaceRepository,
    val enabledWidgetRepository: EnabledWidgetRepository,
    val appSharedPrefs: AppSharedPrefs
    ) : RemoteViewsService.RemoteViewsFactory {

    val TAG = "FavoriteRemoteVF"

    var listItems: List<StopPlaceListItem> = listOf()
    var prefsBlocks: Int? = 3
    var stopGroupId: String? = intent.extras?.get("STOP_IDENTIFIER") as String
    var widgetId: Int = intent.extras?.get("APPWIDGET_ID") as Int? ?: -1

    override fun onCreate() { Log.d(TAG, "$TAG onCreate") }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged triggered for factory:  " + this.hashCode())
        val mStopGroupId = stopGroupId
        val widgetConfig = fetchWidgetConfigBlocking(widgetId) //Blocking call!
        prefsBlocks = fetchCellNumberBlocking()
        Log.d(TAG, "prefsblock number fetched: $prefsBlocks")
        if(mStopGroupId != null){
            Log.d(TAG, "using stop group id $stopGroupId")
            val stopPlace = fetchStopGroupBlocking(mStopGroupId) //Blocking call!
            if(stopPlace != null){
                val stops = stopPlace.stops
                if(stops != null){
                    val mListItems = StopPlaceUtils.createListData(stops)
                    listItems = mListItems
                }
            }
        }
    }

    override fun onDestroy() { }

    override fun getCount(): Int { return listItems.size }

    override fun getViewAt(position: Int): RemoteViews {
        //Log.d(TAG, "getViewAt")
        return when(listItems[position]){
            is StopPlaceListDivider -> {
                //Log.d(TAG, "getViewAt triggered for view with pos $position, created list divider")
                RemoteViews(context.packageName, R.layout.widget_view_stopgroup_divider).apply {
                    setTextViewText(R.id.widget_stop_place_stop_name, (listItems[position] as StopPlaceListDivider).text)
                }
            }
            is StopPlaceListEntry -> {
                val item = listItems[position] as StopPlaceListEntry
                //Log.d(TAG, "getViewAt triggered for view with pos $position and item linenumber ${item.lineNumber} with ${item.displayTimes.size} items")
                createTimeEntryRemoteView(context, item)
            }
            else -> throw IOException("No matching clause")
        }
    }

    override fun getLoadingView(): RemoteViews? { return null }

    override fun getViewTypeCount(): Int { return 2 }

    override fun getItemId(position: Int): Long { return position.toLong() }

    override fun hasStableIds(): Boolean { return true }

    /**
     * Creates a list entry for the remoteview containing stopgroup details.
     */
    private fun createTimeEntryRemoteView(context: Context, data: StopPlaceListEntry) : RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_view_stopgroup_item2).apply {
            removeAllViews(R.id.widget_stop_place2_linearlayout_time_container)
            //Log.d(TAG, "createTimeEntryRemoteView called with item $data")
            setTextViewText(R.id.widget_stop_place2_textview_line_number, data.lineNumber)
            setTextViewText(R.id.widget_stop_place2_textview_line_name, data.directionName)
            // If there aren't any more departures left, display only a string
            if(data.displayTimes.isEmpty()){
                val text = "Ingen flere avganger i dag"
                val textView = RemoteViews(context.packageName, R.layout.remoteview_textview)
                textView.setTextViewText(R.id.remoteview_element_textview, text)
                addView(R.id.widget_stop_place2_linearlayout_time_container, textView)
            } else {
                Log.d(TAG, "Rendering ${prefsBlocks ?: 3} items")
                // Display either the default number of departures or take from config if defined
                val items = data.displayTimes.take(prefsBlocks ?: 3)
                for(i in items.indices){
                    //Log.d(TAG, "setting textview text to ${items[i]}")
                    val rTextView = RemoteViews(context.packageName, R.layout.remoteview_textview)
                    rTextView.setTextViewText(R.id.remoteview_element_textview, items[i])
                    if(data.isEmphasized[i]){
                        rTextView.setTextColor(R.id.remoteview_element_textview, Color.RED)
                    }
                    addView(R.id.widget_stop_place2_linearlayout_time_container, rTextView)
                }
            }
        }
    }

    /**
     * Fetches the relevant widgetId and corresponding name.
     * Queries sharedpreferences for data.
     */
    private fun fetchWidgetConfigBlocking(widgetId: Int): EnabledWidget? {
        var enabledWidget: EnabledWidget?
        runBlocking {
            enabledWidget = enabledWidgetRepository.getEnabledWidget(widgetId)
        }
        if(enabledWidget != null) return enabledWidget
        return null
    }

    /**
     * Fecthes a stopgroup.
     * @param stopGroupId The id of the stopgroup to fetch.
     */
    private fun fetchStopGroupBlocking(stopGroupId: String): StopGroup? {
        var stopGroup: StopGroup?
        runBlocking {
            stopGroup = stopPlaceRepository.fetchStopPlace(stopGroupId)
        }
        if(stopGroup != null) return stopGroup
        return null
    }

    /**
     * Fetches the number of blocks to display in a row entry.
     * Queries sharedpreferences for data, or defaults to 3.
     */
    private fun fetchCellNumberBlocking(): Int? {
        try {
            var cellNumber: String?
            runBlocking {
                cellNumber = appSharedPrefs.readWidgetTimeItemsLimit()
            }
            if(cellNumber != null) {
                return cellNumber?.toInt()
            }
            return 3
        } catch (exception: Throwable){
            Log.d(TAG, exception.stackTraceToString())
            return 3
        }
    }
}