package com.app.skyss_companion.view.planner.location_search

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingProperties
import com.app.skyss_companion.view.routedirection_timetable.PassingTimeListItem

class LocationSearchListAdapter(private val onTap: (Int) -> Unit) : RecyclerView.Adapter<LocationSearchListAdapter.ViewHolder>() {

    val TAG = "PTimeFilterAdapter"
    var dataSet = listOf<GeocodingProperties>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var container: LinearLayout = view.findViewById(R.id.location_item_layout)
        var icon: ImageView = view.findViewById(R.id.location_item_icon)
        var small: TextView = view.findViewById(R.id.location_item_small_text)
        var main : TextView = view.findViewById(R.id.location_item_main_text)
        fun bind(position: Int, onTap: (Int) -> Unit){
            val properties = dataSet[position]
            val propIcon = CategoryIconResolver.resolveIcon(properties.category)
            icon.setImageResource(propIcon)
            small.text = properties.county
            main.text = properties.label
            container.setOnClickListener { onTap(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, onTap)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<GeocodingProperties>){
        Log.d(TAG, "PassingTimeListAdapter setData triggered with ${newItems.size} new items.")
        this.dataSet = newItems
        notifyDataSetChanged()
    }

}