package com.app.skyss_companion.view.stop_place

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R


class StopPlaceLineCodeAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = "StopPlaceAdapter"
    var dataSet = listOf<String>()

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lineNumber: TextView = view.findViewById(R.id.stop_place_linecode_textview)
        fun bind(position: Int){
            lineNumber.text = dataSet[position]
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
        (holder as ViewHolder).bind(position)
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<String>){
        this.dataSet = newItems
        notifyDataSetChanged()
    }

}