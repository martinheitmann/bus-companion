package com.app.skyss_companion.view.stop_place

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R


class StopPlaceLineCodeAdapter(private val context: Context, private val onTap: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = "StopPlaceAdapter"
    var dataSet = listOf<String>()

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lineNumber: TextView = view.findViewById(R.id.route_direction_filter_list_item)
        fun bind(position: Int, onTap: (Int) -> Unit){
            lineNumber.text = dataSet[position]
            lineNumber.setOnClickListener {
                onTap(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.stop_place_linecode_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position, onTap)
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<String>){
        this.dataSet = newItems
        notifyDataSetChanged()
    }

}