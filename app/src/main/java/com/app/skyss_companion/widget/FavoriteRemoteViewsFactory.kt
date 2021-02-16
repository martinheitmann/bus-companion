package com.app.skyss_companion.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.TextView
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.StopPlaceUtils
import com.app.skyss_companion.repository.StopPlaceRepository
import com.app.skyss_companion.view.stop_place.StopPlaceListDivider
import com.app.skyss_companion.view.stop_place.StopPlaceListEntry
import com.app.skyss_companion.view.stop_place.StopPlaceListItem
import kotlinx.coroutines.runBlocking
import java.io.IOException


class FavoriteRemoteViewsFactory(
    intent: Intent,
    val context: Context,
    val stopPlaceRepository: StopPlaceRepository
    ) : RemoteViewsService.RemoteViewsFactory {

    val TAG = "FavoriteRemoteVF"

    var listItems: List<StopPlaceListItem> = listOf()
    var stopGroupId: String? = intent.extras?.get("STOP_IDENTIFIER") as String

    override fun onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
    }
    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged triggered")
        runBlocking {
            val mStopGroupId = stopGroupId
            if(mStopGroupId != null){
                Log.d(TAG, "using stop group id $stopGroupId")
                val stopPlace = stopPlaceRepository.fetchStopPlace(mStopGroupId)
                if(stopPlace != null){
                    val stops = stopPlace.stops
                    if(stops != null){
                        var mListItems = StopPlaceUtils.createListData(stops)
                        listItems = mListItems
                    }
                }
            }
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return listItems.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        return when(listItems[position]){
            is StopPlaceListDivider -> {
                Log.d(TAG, "getViewAt triggered for view with pos $position, created list divider")
                RemoteViews(context.packageName, R.layout.widget_view_stopgroup_divider).apply {
                    setTextViewText(R.id.widget_stop_place_stop_name, (listItems[position] as StopPlaceListDivider).text)
                }
            }
            is StopPlaceListEntry -> {
                val item = listItems[position] as StopPlaceListEntry
                Log.d(TAG, "getViewAt triggered for view with pos $position and item linenumber ${item.lineNumber} with ${item.displayTimes.size} items")
                //createTimeEntryRemoteView(context, item)
                createTimeEntryRemoteView2(context, item)
                //return createSimpleTestView(context, listItems[position] as StopPlaceListEntry)
            }
            else -> throw IOException("No matching clause")
        }
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun createSimpleTestView(context: Context, data: StopPlaceListEntry) : RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_view_stopgroup_simple_1).apply {
            setTextViewText(R.id.textView4_test1, data.lineNumber)
            setTextViewText(R.id.textView6_test2, data.directionName)
            setViewVisibility(R.id.textView11_shouldbeinvisible, View.GONE)
            //setTextViewText(R.id.textView4_test3, "Test1")
            //setTextViewText(R.id.textView6_test4, "Test2")
        }
    }

    private fun createTimeEntryRemoteView(context: Context, data: StopPlaceListEntry) : RemoteViews {

        return RemoteViews(context.packageName, R.layout.widget_view_stopgroup_item).apply {
            Log.d(TAG, "createTimeEntryRemoteView called with item $data")
            setTextViewText(R.id.widget_stop_place_textview_line_number, data.lineNumber)
            setTextViewText(R.id.widget_stop_place_textview_line_name, data.directionName)
            if(data.displayTimes.isEmpty()){
                val text = "Ingen flere avganger i dag"
                setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_empty, text)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.VISIBLE)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_1, View.GONE)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_2, View.GONE)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_3, View.GONE)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_4, View.GONE)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_5, View.GONE)
                setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_6, View.GONE)
            } else {
                val items = data.displayTimes.take(6)
                if(items.size == 1){
                    val viewText1 = items[0]
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.GONE)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_1, viewText1)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_2, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_3, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_4, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_5, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_6, View.INVISIBLE)
                }
                if(items.size == 2){
                    val viewText1 = items[0]
                    val viewText2 = items[1]
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.GONE)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_1, viewText1)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_2, viewText2)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_3, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_4, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_5, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_6, View.INVISIBLE)
                }
                if(items.size == 3){
                    val viewText1 = items[0]
                    val viewText2 = items[1]
                    val viewText3 = items[2]
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.GONE)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_1, viewText1)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_2, viewText2)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_3, viewText3)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_4, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_5, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_6, View.INVISIBLE)
                }
                if(items.size == 4){
                    val viewText1 = items[0]
                    val viewText2 = items[1]
                    val viewText3 = items[2]
                    val viewText4 = items[3]
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.GONE)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_1, viewText1)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_2, viewText2)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_3, viewText3)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_4, viewText4)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_5, View.INVISIBLE)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_6, View.INVISIBLE)
                }
                if(items.size == 5){
                    val viewText1 = items[0]
                    val viewText2 = items[1]
                    val viewText3 = items[2]
                    val viewText4 = items[3]
                    val viewText5 = items[4]
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.GONE)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_1, viewText1)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_2, viewText2)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_3, viewText3)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_4, viewText4)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_5, viewText5)
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_6, View.INVISIBLE)
                }
                if(items.size == 6){
                    val viewText1 = items[0]
                    val viewText2 = items[1]
                    val viewText3 = items[2]
                    val viewText4 = items[3]
                    val viewText5 = items[4]
                    val viewText6 = items[5]
                    setViewVisibility(R.id.widget_stop_place_linearlayout_time_tv_empty, View.GONE)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_1, viewText1)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_2, viewText2)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_3, viewText3)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_4, viewText4)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_5, viewText5)
                    setTextViewText(R.id.widget_stop_place_linearlayout_time_tv_6, viewText6)
                }
            }
        }
    }

    private fun createTimeEntryRemoteView2(context: Context, data: StopPlaceListEntry) : RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_view_stopgroup_item2).apply {
            removeAllViews(R.id.widget_stop_place2_linearlayout_time_container)
            Log.d(TAG, "createTimeEntryRemoteView called with item $data")
            setTextViewText(R.id.widget_stop_place2_textview_line_number, data.lineNumber)
            setTextViewText(R.id.widget_stop_place2_textview_line_name, data.directionName)
            if(data.displayTimes.isEmpty()){
                val text = "Ingen flere avganger i dag"
                val textView = RemoteViews(context.packageName, R.layout.remoteview_textview)
                textView.setTextViewText(R.id.remoteview_element_textview, text)
                addView(R.id.widget_stop_place2_linearlayout_time_container, textView)
            } else {
                val items = data.displayTimes.take(6)
                for(i in items.indices){
                    Log.d(TAG, "setting textview text to ${items[i]}")
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
}