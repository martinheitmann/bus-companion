package com.app.skyss_companion.misc

import androidx.recyclerview.widget.DiffUtil
import com.app.skyss_companion.model.StopGroup

class SearchStopsDiffUtilCallback(
    private val oldStopGroups: List<StopGroup>,
    private val newStopGroups: List<StopGroup>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldStopGroups.size
    }

    override fun getNewListSize(): Int {
        return newStopGroups.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStopGroups[oldItemPosition].identifier == newStopGroups[newItemPosition].identifier
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldStopGroups[oldItemPosition] == newStopGroups[newItemPosition]
    }
}