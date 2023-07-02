package com.app.skyss_companion.view.planner

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.misc.LogcatUtils
import com.app.skyss_companion.model.travelplanner.End
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelStep
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class TravelPlannerListAdapter(private val context: Context, private val onTap: (Int) -> Unit) :
    RecyclerView.Adapter<TravelPlannerListAdapter.ViewHolder>() {
    val tag = "TPListAdapter"
    var dataSet = listOf<TravelPlan>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var date: TextView = view.findViewById(R.id.travel_plan_list_item_date)
        var duration: TextView = view.findViewById(R.id.travel_plan_list_item_duration)
        var flex: FlexboxLayout = view.findViewById(R.id.travel_plan_list_item_flex)
        var card: CardView = view.findViewById(R.id.travel_plan_list_item_card)
        fun bind(position: Int, onTap: (Int) -> Unit) {
            val tp = dataSet[position]
            card.setOnClickListener { onTap(position) }
            date.text = TravelPlannerUtils.getTimeDurationString(tp)
            duration.text = TravelPlannerUtils.getDurationMinutes(tp)
            flex.removeAllViews()
            Log.d(tag, "--------------------------------------")
            tp.travelSteps.forEachIndexed { idx, stp ->
                val newView = getLayoutElement(stp, context, flex)
                flex.addView(newView)
                Log.d(tag, "position $position/${dataSet.lastIndex}, list size ${dataSet.size}")
                if (idx != tp.travelSteps.lastIndex) {
                    flex.addView(getLayoutNext(context, flex))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.travel_plan_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, onTap)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<TravelPlan>) {
        this.dataSet = newItems
        val stepsOnly = newItems.map { i -> i.travelSteps }
        LogcatUtils.printStringInSegments(string = Gson().toJson(stepsOnly), tag = tag)
        notifyDataSetChanged()
    }


    fun getLayoutElement(travelStep: TravelStep, context: Context, root: ViewGroup): View {
        val element: View
        val inflater = LayoutInflater.from(context)
        when (travelStep.type) {
            "route" -> {
                //element = View.inflate(context, R.layout.travel_plan_list_item_bus, root)
                element = inflater.inflate(R.layout.travel_plan_list_item_route, root, false)
                val elementText: TextView =
                    element.findViewById(R.id.travel_plan_list_item_route_text)
                elementText.text = travelStep.routeDirection?.publicIdentifier ?: "?"
            }

            "walk" -> {
                //element = View.inflate(context, R.layout.travel_plan_list_item_walk, root)
                element = inflater.inflate(R.layout.travel_plan_list_item_walk, root, false)
            }

            else -> {
                //element = View.inflate(context, R.layout.travel_plan_list_item_unknown, root)
                element = inflater.inflate(R.layout.travel_plan_list_item_unknown, root, false)
            }
        }
        return element
    }

    fun getLayoutNext(context: Context, root: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.travel_plan_list_item_next, root, false)
    }
}