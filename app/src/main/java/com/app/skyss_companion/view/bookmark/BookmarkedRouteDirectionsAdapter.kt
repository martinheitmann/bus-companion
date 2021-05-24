package com.app.skyss_companion.view.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.BookmarkedRouteDirection

class BookmarkedRouteDirectionsAdapter(private val onItemTapped: (BookmarkedRouteDirection) -> Unit) : RecyclerView.Adapter<BookmarkedRouteDirectionsAdapter.ViewHolder>() {
    var dataSet = listOf<BookmarkedRouteDirection>()

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: View = view.findViewById(R.id.bookmarked_routedirection_framelayout)
        var stopGroupName: TextView = view.findViewById(R.id.bookmarked_routedirection_stopgroup_name)
        var routeDirectionName: TextView = view.findViewById(R.id.bookmarked_routedirection_name)
        var lineCode: TextView = view.findViewById(R.id.bookmarked_routedirection_linecode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_routedirection_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.stopGroupName.text = dataSet[position].stopGroupName
        holder.layout.setOnClickListener { onItemTapped(dataSet[position]) }
        holder.routeDirectionName.text = dataSet[position].routeDirectionName
        holder.lineCode.text =  dataSet[position].lineCode

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<BookmarkedRouteDirection>){
        dataSet = newItems
        notifyDataSetChanged()
    }
}