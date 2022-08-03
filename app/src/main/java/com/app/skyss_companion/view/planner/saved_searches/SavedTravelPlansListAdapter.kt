package com.app.skyss_companion.view.planner.location_search

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import java.time.format.DateTimeFormatter

class SavedTravelPlansListAdapter(
    private val onTap: (Int) -> Unit,
    private val onDeleteButtonTap: (Int) -> Unit
) : RecyclerView.Adapter<SavedTravelPlansListAdapter.ViewHolder>() {

    val TAG = "SavedTPListAdapter"
    var dataSet = listOf<BookmarkedTravelPlan>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var delete: ImageView = view.findViewById(R.id.bookmarked_travel_plan_list_item_delete)
        var from: TextView = view.findViewById(R.id.bookmarked_travel_plan_list_item_from_text)
        var to: TextView = view.findViewById(R.id.bookmarked_travel_plan_list_item_to_text)
        var timestamp: TextView = view.findViewById(R.id.bookmarked_travel_plan_list_item_timestamp)
        var layout: FrameLayout = view.findViewById(R.id.bookmarked_travel_plan_list_item_frame)
        fun bind(position: Int, onTap: (Int) -> Unit, onDeleteButtonTap: (Int) -> Unit) {
            val item = dataSet[position]
            layout.setOnClickListener { onTap(position) }
            from.text = item.fromFeature.properties.label
            to.text = item.toFeature.properties.label
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            timestamp.text = formatter.format(item.createdAt)
            delete.setOnClickListener { onDeleteButtonTap(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmarked_travelplan_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, onTap, onDeleteButtonTap)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<BookmarkedTravelPlan>) {
        Log.d(TAG, "SavedTravelPlansListAdapter setData triggered with ${newItems.size} new items.")
        this.dataSet = newItems.sortedBy { it.createdAt }
        notifyDataSetChanged()
    }

}