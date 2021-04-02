package com.app.skyss_companion.view.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.misc.SearchStopsDiffUtilCallback
import com.app.skyss_companion.model.StopGroup


class SearchViewAdapter(private val onItemTapped: (String) -> Unit) : RecyclerView.Adapter<SearchViewAdapter.ViewHolder>() {
    val TAG = "SearchViewAdapter"
    val dataSet = mutableListOf<StopGroup>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: View = view.findViewById(R.id.search_stops_list_element_framelayout)
        var title: TextView = view.findViewById(R.id.list_element_title)
        var busImage: ImageView = view.findViewById(R.id.imageview_bus)
        var railImage: ImageView = view.findViewById(R.id.imageview_rail)
        fun bind(position: Int){
            Log.d(TAG, "Item position: $position, title: ${dataSet[position].description}")
            title.text = dataSet[position].description
            layout.setOnClickListener { onItemTapped(dataSet[position].identifier) }
            if(dataSet[position].serviceModes?.contains("Bus") == true){
                busImage.visibility = View.VISIBLE
            } else {
                busImage.visibility = View.GONE
            }
            if(dataSet[position].serviceModes?.contains("Light rail") == true){
                railImage.visibility = View.VISIBLE
            } else {
                railImage.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_stops_list_element, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "Adapter data count: " + dataSet.size)
        return dataSet.size
    }

    fun setData(newItems: List<StopGroup>){
        //val diffResult = DiffUtil.calculateDiff(SearchStopsDiffUtilCallback(this.dataSet, newItems), true)
        dataSet.clear()
        dataSet.addAll(newItems)
        notifyDataSetChanged()
        //diffResult.dispatchUpdatesTo(this)
    }

    /*fun setData(newItems: List<StopGroup>){
        if(newItems.isEmpty()){
            dataSet = newItems
            notifyDataSetChanged()
        } else {
            val diffResult = DiffUtil.calculateDiff(SearchStopsDiffUtilCallback(this.dataSet, newItems))
            dataSet = newItems
            diffResult.dispatchUpdatesTo(this)
        }
    }*/

}