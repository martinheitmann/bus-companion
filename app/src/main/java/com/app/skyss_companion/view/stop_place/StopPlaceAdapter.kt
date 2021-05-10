package com.app.skyss_companion.view.stop_place

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import kotlin.reflect.KFunction2


class StopPlaceAdapter(private val context: Context, private val onTap: (String?, String?, String?, String?, String?) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TAG = "StopPlaceAdapter"
    var dataSet = listOf<StopPlaceListItem>()

    // List divider
    private inner class ViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        var stopName: TextView = view.findViewById(R.id.stop_place_stop_name)
        fun bind(position: Int){
            stopName.text = (dataSet[position] as StopPlaceListDivider).text
        }
    }

    // List entry
    private inner class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        var lineNumber: TextView = view.findViewById(R.id.widget_stop_place_textview_line_number)
        var lineName: TextView = view.findViewById(R.id.widget_stop_place_textview_line_name)
        var timeContainer: LinearLayout = view.findViewById(R.id.widget_stop_place_linearlayout_time_container)
        var cardViewContainer: CardView = view.findViewById(R.id.widget_stop_place_cardview)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int){
            timeContainer.removeAllViews()
            val data = dataSet[position] as StopPlaceListEntry
            cardViewContainer.setOnClickListener {
                onTap(data.stopIdentifier, data.routeDirectionIdentifier, data.stopName, data.directionName, data.lineNumber)
            }
            lineNumber.text = data.lineNumber.toString()
            lineName.text = data.directionName
            if(data.displayTimes.isEmpty()){
                val tw = TextView(context)
                tw.text = "Ingen flere avganger i dag"
                val lpar = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lpar.weight = 1F
                tw.layoutParams = lpar
                timeContainer.addView(tw)
            } else {
                data.displayTimes.take(6).forEachIndexed { index, text ->
                    val tw = TextView(context)
                    tw.text = text
                    val lpar = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lpar.setMargins(16)
                    tw.layoutParams = lpar
                    if(data.isEmphasized[index]){
                        tw.setTypeface(null, Typeface.BOLD)
                    }
                    timeContainer.addView(tw)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        if(dataSet[position] is StopPlaceListEntry){
            return 2
        }
        if(dataSet[position] is StopPlaceListDivider){
            return 1
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Create a new view, which defines the UI of the list item
        if (viewType == 1) {
            return ViewHolder1(
                LayoutInflater.from(context).inflate(
                    R.layout.stop_place_list_divider,
                    parent,
                    false
                )
            )
        }
        return ViewHolder2(
            LayoutInflater.from(context).inflate(R.layout.stop_place_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 1) {
            (holder as ViewHolder1).bind(position)
        } else {
            (holder as ViewHolder2).bind(position)
        }
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<StopPlaceListItem>){
        this.dataSet = newItems
        notifyDataSetChanged()
    }

}