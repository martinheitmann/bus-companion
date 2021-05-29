package com.app.skyss_companion.view.routedirection_timetable

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R

class PassingTimeListAdapter(private val context: Context, private val onTap: (Int) -> Unit) : RecyclerView.Adapter<PassingTimeListAdapter.ViewHolder>(){
    val TAG = "PTimeFilterAdapter"
    var dataSet = listOf<PassingTimeListItem>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card: CardView = view.findViewById(R.id.route_direction_time_table_list_item_card)
        var displayTime: TextView = view.findViewById(R.id.route_direction_time_table_list_item_textview)
        fun bind(position: Int, onTap: (Int) -> Unit){
            displayTime.text = dataSet[position].displayTime
            displayTime.setOnClickListener {
                onTap(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.route_direction_time_table_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, onTap)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<PassingTimeListItem>){
        Log.d(TAG, "PassingTimeListAdapter setData triggered with ${newItems.size} new items.")
        this.dataSet = newItems
        notifyDataSetChanged()
    }
}