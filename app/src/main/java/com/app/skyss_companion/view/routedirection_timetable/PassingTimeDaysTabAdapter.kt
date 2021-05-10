package com.app.skyss_companion.view.routedirection_timetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import kotlin.reflect.KFunction0


class PassingTimeDaysTabAdapter(private val context: Context, private val onTap: (Int) -> Unit) : RecyclerView.Adapter<PassingTimeDaysTabAdapter.ViewHolder>() {
    val TAG = "PassingTimeFilterAdapter"
    var dataSet = listOf<PassingTimeDayTab>()
    //var selectedIndex = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lineNumber: TextView = view.findViewById(R.id.route_direction_filter_list_item)
        fun bind(position: Int, onTap: (Int) -> Unit){
            lineNumber.text = dataSet[position].display
            lineNumber.setOnClickListener {
                onTap(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.route_direction_time_table_filter_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, onTap)
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<PassingTimeDayTab>){
        this.dataSet = newItems
        notifyDataSetChanged()
    }

}