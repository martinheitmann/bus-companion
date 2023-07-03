package com.app.skyss_companion.widget.xml.travelplanner

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.app.skyss_companion.R
import com.app.skyss_companion.model.EnabledWidget
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelPlannerRoot
import com.app.skyss_companion.model.travelplanner.TravelStep
import com.app.skyss_companion.prefs.AppSharedPrefs
import com.app.skyss_companion.repository.EnabledWidgetRepository
import com.app.skyss_companion.repository.TravelPlannerRepository
import com.app.skyss_companion.view.planner.TravelPlannerUtils
import com.app.skyss_companion.view.planner.selected_plan.SelectedTravelPlanFragment
import kotlinx.coroutines.runBlocking


class TravelPlannerWidgetRemoteViewsFactory(
    intent: Intent,
    val context: Context,
    private val travelPlannerRepository: TravelPlannerRepository,
    val enabledWidgetRepository: EnabledWidgetRepository,
    val appSharedPrefs: AppSharedPrefs
) : RemoteViewsService.RemoteViewsFactory {

    val TAG = "TPRemoteViewsFactory"
    var rowItemCount: Int? = null
    var travelPlans: List<TravelPlan> = listOf()
    var widgetId: Int = intent.extras?.get("APPWIDGET_ID") as Int? ?: -1

    override fun onCreate() {
        Log.d(TAG, "$TAG onCreate triggered")
    }

    override fun onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged triggered for factory " + this.hashCode())
        val widgetConfig = fetchWidgetConfigBlocking(widgetId) //Blocking call!
        rowItemCount = fetchRowItemCountBlocking()
        if (widgetConfig != null) {
            val travelPlanRoot = fetchTravelPlansBlocking(widgetConfig) //Blocking call!
            if (travelPlanRoot != null) {
                travelPlans = travelPlanRoot.travelPlans
            }
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return travelPlans.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        //Log.d(TAG, "getViewAt")
        return createTravelPlannerEntryRemoteView(context, travelPlans[position])
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun createTravelPlannerEntryRemoteView(
        context: Context,
        travelPlan: TravelPlan
    ): RemoteViews {
        Log.d(
            TAG,
            "creating remoteviews for travelplan with id ${travelPlan.id}, start time ${travelPlan.startTime} and end time start time ${travelPlan.endTime}."
        )
        return RemoteViews(context.packageName, R.layout.widget_travelplans_view_list_item).apply {
            travelPlan.id?.let { id ->
                val intent = Intent().apply {
                    putExtra("travelPlanId", id)
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                }
                setOnClickFillInIntent(R.id.widget_travel_plan_list_item_button_more, intent)
                Log.d(TAG, "Set pending intent for travel planner id $id.")
            } ?: run {
                Log.d(TAG, "WARNING: No travel plan id attached, skipping pending intent.")
            }
            removeAllViews(R.id.widget_travel_plan_list_item_lin_container)
            setTextViewText(
                R.id.widget_travel_plan_list_item_date,
                TravelPlannerUtils.getTimeDurationString(travelPlan)
            )
            setTextViewText(
                R.id.widget_travel_plan_list_item_duration,
                TravelPlannerUtils.getDurationMinutes(travelPlan)
            )
            val remoteViews = createRows(travelPlan.travelSteps)
            for (view in remoteViews) {
                addView(R.id.widget_travel_plan_list_item_lin_container, view)
            }
        }
    }

    private fun createRows(travelSteps: List<TravelStep>): List<RemoteViews> {
        val chunked = travelSteps.chunked(rowItemCount ?: 3)
        return chunked.mapIndexed { index, chunk ->
            createRow(
                chunk,
                index == chunked.lastIndex
            )
        }
    }

    private fun createRow(
        travelSteps: List<TravelStep>,
        isLastRow: Boolean
    ): RemoteViews {
        val linearLayoutRV = createLinearLayoutContainerRemoteViews()
        travelSteps.forEachIndexed { index, travelStep ->
            when (travelStep.type) {
                "route" -> {
                    val routeDescription = travelStep.routeDirection?.publicIdentifier ?: "?"
                    val view = createRouteRemoteViews(routeDescription)
                    linearLayoutRV.addView(R.id.remoteviews_linearlayout_main, view)
                }
                "walk" -> {
                    val view = createWalkRemoteViews()
                    linearLayoutRV.addView(R.id.remoteviews_linearlayout_main, view)
                }
            }
            if (index == travelSteps.lastIndex && isLastRow) {
                /* no-op */
            } else {
                val next = createDividerRemoteViews()
                linearLayoutRV.addView(R.id.remoteviews_linearlayout_main, next)
            }
        }
        return linearLayoutRV
    }

    private fun createLinearLayoutContainerRemoteViews(): RemoteViews {
        return RemoteViews(
            context.packageName,
            R.layout.remoteview_linearlayout_match_length_wrap_height
        )
    }

    private fun createPendingIntent(context: Context, travelPlanId: String): PendingIntent {
        val intent = Intent(context, SelectedTravelPlanFragment::class.java).apply {
            putExtra("travelPlanId", travelPlanId)
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            FLAG_IMMUTABLE or Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }

    private fun createRouteRemoteViews(routeText: String): RemoteViews {
        val routeLayout =
            RemoteViews(context.packageName, R.layout.widget_travelplans_view_list_item_route)
        routeLayout.setTextViewText(R.id.widget_travelplans_list_item_route_text, routeText)
        return routeLayout
    }

    private fun createWalkRemoteViews(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_travelplans_view_list_item_walk)
    }

    private fun createDividerRemoteViews(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_travelplans_view_list_item_next)
    }


    private fun fetchWidgetConfigBlocking(widgetId: Int): EnabledWidget? {
        Log.d(TAG, "fetching widget config.")
        var enabledWidget: EnabledWidget?
        runBlocking {
            enabledWidget = enabledWidgetRepository.getEnabledWidget(widgetId)
        }
        if (enabledWidget != null) return enabledWidget
        return null
    }

    private fun fetchTravelPlansBlocking(data: EnabledWidget): TravelPlannerRoot? {
        Log.d(TAG, "fetching widget travel plans.")
        var travelPlannerRoot: TravelPlannerRoot?
        runBlocking {
            travelPlannerRoot = travelPlannerRepository.getTravelPlansFromWidgetConfig(data)
        }
        if (travelPlannerRoot != null) {
            Log.d(
                TAG,
                "fetched travelplan root with ${travelPlannerRoot?.travelPlans?.size} travel plans."
            )
        }
        return travelPlannerRoot
    }

    private fun fetchRowItemCountBlocking(): Int {
        var cellNumber: Int?
        runBlocking {
            cellNumber = appSharedPrefs.readWidgetRowItemCount()
        }
        return cellNumber ?: 3
    }
}