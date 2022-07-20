package com.app.skyss_companion.view.planner.location_search

import com.app.skyss_companion.R

class CategoryIconResolver {
    companion object {
        fun resolveIcon(categories: List<String>): Int {
            return if(categories.contains("tettsted")) R.drawable.ic_baseline_house_24
            else if (categories.contains("onstreetBus") || categories.contains("busStation")) R.drawable.ic_baseline_directions_bus_24
            else if(categories.contains("onstreetTram")) R.drawable.ic_baseline_directions_railway_24
            else if (categories.contains("poi")|| categories.contains("street")) R.drawable.ic_baseline_location_on_24
            else R.drawable.ic_baseline_help_outline_24
        }
    }
}