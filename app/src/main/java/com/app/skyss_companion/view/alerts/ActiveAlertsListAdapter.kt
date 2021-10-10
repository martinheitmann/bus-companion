package com.app.skyss_companion.view.alerts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.model.PassingTimeAlert
import com.app.skyss_companion.view.bookmark.BookmarkedRouteDirectionsAdapter

class ActiveAlertsListAdapter(private val onDelete: (PassingTimeAlert) -> Unit) : RecyclerView.Adapter<ActiveAlertsListAdapter.ViewHolder>() {

    var dataSet = listOf<PassingTimeAlert>()

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var stopPlaceName: TextView = view.findViewById(R.id.active_alerts_item_stop_place)
        var lineNumber: TextView = view.findViewById(R.id.active_alerts_item_linenumber)
        var lineName: TextView = view.findViewById(R.id.active_alerts_item_linename)
        var alertTime: TextView = view.findViewById(R.id.active_alerts_item_alert_time)
        var routeTime: TextView = view.findViewById(R.id.active_alerts_item_route_time)
        var delete: ImageButton = view.findViewById(R.id.active_alerts_item_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.active_alerts_list_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.stopPlaceName.text = item.stopName
        holder.lineNumber.text = item.lineNumber
        holder.lineName.text = item.directionName
        // Add leading 0's to the numbers for correct time formatting.
        holder.alertTime.text = "${String.format("%02d", item.zonedAlertTimestamp.hour)}:${String.format("%02d", item.zonedAlertTimestamp.minute)}"
        holder.routeTime.text = "${String.format("%02d", item.zonedRouteTimestamp.hour)}:${String.format("%02d", item.zonedRouteTimestamp.minute)}"
        holder.delete.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<PassingTimeAlert>){
        dataSet = newItems
        notifyDataSetChanged()
    }
}