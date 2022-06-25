package com.app.skyss_companion.view.planner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.model.travelplanner.End
import com.app.skyss_companion.model.travelplanner.Intermediate
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelStep
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration


class SelectedTravelPlanAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val tag = "PTimeFilterAdapter"
    var dataSet = listOf<TravelStep>()
    var expandedIntermediates = mutableListOf<Boolean>()
    var end: End? = null

    inner class WalkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val walkStopName: TextView = view.findViewById(R.id.selected_travel_plan_walk_tw_stop_name)
        val walkDuration: TextView = view.findViewById(R.id.selected_travel_plan_walk_tw_minutes)
        val walkDistance: TextView = view.findViewById(R.id.selected_travel_plan_walk_tw_distance)
        val endName: TextView = view.findViewById(R.id.selected_travel_plan_walk_tw_end_stop_name)
        val endTime: TextView = view.findViewById(R.id.selected_travel_plan_walk_tw_end_stop_time)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val element: TravelStep = dataSet[position]
            Log.d(tag, "WalkViewHolder data = $element")
            walkStopName.text = element.stop?.description
            walkDistance.text = "(${element.distance} meter)"
            walkDuration.text = element.duration?.let { getDurationMinutes(Duration.parse(it)) }
            if (dataSet.lastIndex == position) {
                endTime.text = getCurrentStepWalkEndTime(
                    position,
                    dataSet
                )
                endName.text = getCurrentStepWalkEndName(end)
                endTime.visibility = View.VISIBLE
                endName.visibility = View.VISIBLE
            } else {
                endTime.visibility = View.GONE
                endName.visibility = View.GONE
            }
        }
    }

    inner class RouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val routeDirectionName: TextView =
            view.findViewById(R.id.selected_travel_plan_route_tw_direction_name)
        private val routeLineCode: TextView =
            view.findViewById(R.id.selected_travel_plan_route_tw_linecode)
        private val expandButton: Button =
            view.findViewById(R.id.selected_travel_plan_route_expand_button)
        private val intermediatesList: LinearLayout =
            view.findViewById(R.id.selected_travel_plan_route_between_stops_list)
        private val startTime: TextView =
            view.findViewById(R.id.selected_travel_plan_route_tw_start_stop_time)
        private val startName: TextView =
            view.findViewById(R.id.selected_travel_plan_route_tw_start_stop_name)
        private val endName: TextView =
            view.findViewById(R.id.selected_travel_plan_route_tw_end_stop_name)
        private val endTime: TextView =
            view.findViewById(R.id.selected_travel_plan_route_tw_end_stop_time)

        fun bind(position: Int) {
            val element: TravelStep = dataSet[position]
            routeDirectionName.text = element.routeDirection?.directionName
            routeLineCode.text = element.routeDirection?.publicIdentifier
            startTime.text = getCurrentStepRouteStartTime(
                position,
                dataSet,
            )
            startName.text = getCurrentStepRouteStartName(
                position,
                dataSet,
            )
            endName.text = getCurrentStepRouteEndName(
                position,
                dataSet,
                end
            ).let { pair -> "${pair.first ?: ""} ${pair.second ?: ""}" }
            endTime.text = getCurrentStepRouteEndTime(
                position,
                dataSet,
            )
            Log.d(
                tag,
                "Travel step with starting stop ${element.stop?.description} had ${element.intermediates.size} intermediates"
            )
            val intermediates = getIntermediateListLayout(element.intermediates, context)
            Log.d(tag, "Setting intermediates of size ${intermediates.size}")
            if (intermediates.isNotEmpty()) {
                expandButton.text = "${intermediates.size} stopp"
                expandButton.setOnClickListener { toggleIntermediates(position) }
                expandButton.visibility = View.VISIBLE
            } else {
                expandButton.visibility = View.GONE
            }
            intermediatesList.removeAllViews()
            if (!expandedIntermediates[position]) {
                intermediatesList.visibility = View.GONE
                val imgResource: Int = R.drawable.ic_baseline_expand_more_24
                expandButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0)
            } else {
                val imgResource: Int = R.drawable.ic_baseline_expand_less_24
                expandButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgResource, 0)
                intermediatesList.visibility = View.VISIBLE
            }
            for (intermediate in intermediates) {
                intermediatesList.addView(intermediate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            RouteViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.selected_travel_plan_route,
                    parent,
                    false
                )
            )
        } else {
            WalkViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.selected_travel_plan_walk, parent, false)
            )
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet[position].type == "walk") {
            return 2
        }
        if (dataSet[position].type == "route") {
            return 1
        }
        return -1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 1) {
            (holder as SelectedTravelPlanAdapter.RouteViewHolder).bind(position)
        } else {
            (holder as SelectedTravelPlanAdapter.WalkViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleIntermediates(position: Int) {
        this.expandedIntermediates[position] = !expandedIntermediates[position]
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<TravelStep>, end: End?) {
        this.dataSet = newItems
        this.expandedIntermediates = dataSet.map { _ -> false }.toMutableList()
        this.end = end
        notifyDataSetChanged()
    }

    fun getTimeDurationString(travelPlan: TravelPlan): String {
        val start = travelPlan.startTime
        val end = travelPlan.endTime
        if (start != null && end != null) {
            val localStart = start.toLocalTime()
            val localEnd = end.toLocalTime()
            return "${localStart.hour}:${localStart.minute} - ${localEnd.hour}:${localStart.minute}"
        }
        return "Ukjent varighet..."
    }

    fun getDurationMinutes(travelPlan: TravelPlan): String {
        val start = travelPlan.startTime
        val end = travelPlan.endTime
        if (start != null && end != null) {
            val localStart = start.toLocalDateTime()
            val localEnd = end.toLocalDateTime()
            val duration = ChronoUnit.MINUTES.between(localStart, localEnd)
            return "(${duration.toInt()} minutter)"
        }
        return "N/A"
    }

    fun getIntermediateListLayout(
        intermediates: List<Intermediate>,
        context: Context
    ): List<View> {
        Log.d(tag, "creating intermediates from list of size ${intermediates.size}")
        //val layouts: MutableList<LinearLayout> = mutableListOf()
        val layouts: MutableList<View> = mutableListOf()
        for (intermediate in intermediates) {
            val linearLayout = LinearLayout(context)

            linearLayout.orientation = LinearLayout.HORIZONTAL
            val linLayoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            linLayoutParams.marginEnd = 8
            linLayoutParams.marginStart = 8
            linLayoutParams.bottomMargin = 16

            linearLayout.layoutParams = linLayoutParams
            val twIntermediateTime = TextView(context)
            val twIntermediateName = TextView(context)
            twIntermediateName.text = intermediate.stopName
            twIntermediateTime.text = intermediate.aimedTime
            twIntermediateName.setTypeface(twIntermediateName.typeface, Typeface.BOLD);
            twIntermediateTime.setTypeface(twIntermediateTime.typeface, Typeface.BOLD);

            val twParamsTime = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            )
            twParamsTime.marginEnd = 32
            val twParamsName = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            twIntermediateTime.layoutParams = twParamsTime
            twIntermediateName.layoutParams = twParamsName
            linearLayout.addView(twIntermediateTime)
            linearLayout.addView(twIntermediateName)
            layouts.add(linearLayout)
        }
        Log.d(tag, "returning ${layouts.size} view elements for intermediates")
        return layouts.toList()
    }

    fun toZonedDateTime(timestamp: String): ZonedDateTime {
        return DateUtils.parseUtcDateStringToEuOsloTimeZone(timestamp)
    }

    fun getDurationMinutes(d: Duration): String {
        return if (d.inWholeMinutes.toInt() == 1) "1 minutt"
        else "${d.inWholeMinutes.toInt()} minutter"
    }

    fun getCurrentStepRouteEndTime(currentIndex: Int, steps: List<TravelStep>): String? {
        val hour = steps[currentIndex].endTime?.hour.toString().padStart(2, '0')
        val minute = steps[currentIndex].endTime?.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }

    fun getCurrentStepRouteStartName(currentIndex: Int, steps: List<TravelStep>): String {
        return "${steps[currentIndex].stop?.description ?: ""} ${steps[currentIndex].stop?.platform ?: ""}"
    }

    fun getCurrentStepRouteStartTime(currentIndex: Int, steps: List<TravelStep>): String? {
        val hour = steps[currentIndex].startTime?.hour.toString().padStart(2, '0')
        val minute = steps[currentIndex].startTime?.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }

    fun getCurrentStepWalkEndName(end: End?): String {
        return "${end?.description ?: ""} ${end?.platform ?: ""}"
    }

    fun getCurrentStepWalkEndTime(currentIndex: Int, steps: List<TravelStep>): String {
        val hour = steps[currentIndex].endTime?.hour.toString().padStart(2, '0')
        val minute = steps[currentIndex].endTime?.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }

    fun getCurrentStepRouteEndName(
        currentIndex: Int,
        steps: List<TravelStep>,
        end: End?
    ): Pair<String?, String?> {
        return if (currentIndex == steps.lastIndex) {
            Pair(end?.description, end?.platform)
        } else {
            val nextStep = steps[currentIndex + 1]
            Pair(nextStep.stop?.description, nextStep.stop?.platform)
        }
    }
}