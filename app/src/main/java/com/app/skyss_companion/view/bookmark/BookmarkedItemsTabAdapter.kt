package com.app.skyss_companion.view.bookmark

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.skyss_companion.view.bookmark.routedirections.BookmarkedRouteDirectionsListFragment
import com.app.skyss_companion.view.bookmark.stopgroups.BookmarkedStopGroupsListFragment
import java.io.IOException

class BookmarkedItemsTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val bookmarkedRouteDirectionsFragment = BookmarkedRouteDirectionsListFragment()
        val bookmarkedStopGroupsFragment = BookmarkedStopGroupsListFragment()

        Log.d("TabsContainerAdapter", "current position -> $position")

        return when(position){
            0 -> bookmarkedStopGroupsFragment
            1 -> bookmarkedRouteDirectionsFragment
            else -> throw IOException("Invalid index passed to fragment state adapter.")
        }
    }
}