package com.app.skyss_companion.view.favorites

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

class FavoritesWidgetConfigAdapter(private val onItemTapped: (String) -> Unit) : RecyclerView.Adapter<FavoritesWidgetConfigAdapter.ViewHolder>() {
    var dataSet = listOf<StopGroup>()

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card: View = view.findViewById(R.id.widget_config_list_item_card)
        var title: TextView = view.findViewById(R.id.widget_config_list_item_text)
        var busImage: ImageView = view.findViewById(R.id.imageview_bus)
        var railImage: ImageView = view.findViewById(R.id.imageview_rail)
        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_favorite_config_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].description
        holder.card.setOnClickListener { onItemTapped(dataSet[position].identifier) }
        if(dataSet[position].serviceModes?.contains("Bus") == true){
            holder.busImage.visibility = View.VISIBLE
        } else {
            holder.busImage.visibility = View.GONE
        }
        if(dataSet[position].serviceModes?.contains("Light rail") == true){
            holder.railImage.visibility = View.VISIBLE
        } else {
            holder.railImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newItems: List<StopGroup>){
        if(newItems.isEmpty()){
            dataSet = newItems
            notifyDataSetChanged()
        } else {
            val diffResult = DiffUtil.calculateDiff(SearchStopsDiffUtilCallback(this.dataSet, newItems))
            dataSet = newItems
            diffResult.dispatchUpdatesTo(this)
        }
    }

}