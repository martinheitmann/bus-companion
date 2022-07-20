package com.app.skyss_companion.view

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.skyss_companion.prefs.SettingsFragment
import com.app.skyss_companion.view.alerts.ActiveAlertsFragment
import com.app.skyss_companion.view.bookmark.BookmarkedItemsFragment
import com.app.skyss_companion.view.planner.TravelPlannerFragment
import com.app.skyss_companion.view.search.SearchStopsFragment
import java.io.IOException

class TabsContainerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        val searchFragment = SearchStopsFragment()
        val favoritesFragment = BookmarkedItemsFragment()
        val settingsFragment = SettingsFragment()
        val alertsFragment = ActiveAlertsFragment()
        val plannerFragment = TravelPlannerFragment()

        Log.d("TabsContainerAdapter", "current position -> $position")

        return when(position){
            0 -> favoritesFragment
            1 -> searchFragment
            2 -> alertsFragment
            3 -> plannerFragment
            4 -> settingsFragment
            else -> throw IOException("Invalid index passed to fragment state adapter.")
        }
    }
}