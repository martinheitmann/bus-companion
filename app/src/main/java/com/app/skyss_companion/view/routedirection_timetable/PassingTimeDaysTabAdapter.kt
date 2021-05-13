package com.app.skyss_companion.view.routedirection_timetable

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R


class PassingTimeDaysTabAdapter(private val context: Context, private val onTap: (Int) -> Unit) : RecyclerView.Adapter<PassingTimeDaysTabAdapter.ViewHolder>() {
    val TAG = "PassingTimeFilterAdapter"
    var dataSet = listOf<PassingTimeDayTab>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var lineNumber: TextView = view.findViewById(R.id.route_direction_filter_list_item)
        fun bind(position: Int, onTap: (Int) -> Unit){
            lineNumber.text = dataSet[position].display
            lineNumber.setOnClickListener {
                onTap(position)
            }
            if(dataSet[position].isSelected){
                lineNumber.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.textview_date_rounded_primaryvariant
                )
                val typedValue = TypedValue()
                context.theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
                lineNumber.setTextColor(typedValue.data)
            } else {
                lineNumber.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.textview_date_rounded
                )
                val typedValue = TypedValue()
                context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                lineNumber.setTextColor(typedValue.data)
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