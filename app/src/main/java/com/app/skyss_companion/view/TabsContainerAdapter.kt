package com.app.skyss_companion.view

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.skyss_companion.view.favorites.FavoritesFragment
import com.app.skyss_companion.view.search.SearchStopsFragment
import java.io.IOException

class TabsContainerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val searchFragment = SearchStopsFragment()
        val favoritesFragment = FavoritesFragment()

        Log.d("TabsContainerAdapter", "current position -> $position")

        return when(position){
            0 -> favoritesFragment
            1-> searchFragment
            else -> throw IOException("Invalid index passed to fragment state adapter.")
        }
    }
}