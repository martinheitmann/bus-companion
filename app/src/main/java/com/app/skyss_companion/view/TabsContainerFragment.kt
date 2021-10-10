package com.app.skyss_companion.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.app.skyss_companion.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsContainerFragment : Fragment() {

    private lateinit var tabsContainerAdapter: TabsContainerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabs_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.tabs_container_viewpager2)
        tabLayout = view.findViewById(R.id.tabs_container_tablayout)

        tabsContainerAdapter = TabsContainerAdapter(this)
        viewPager.adapter = tabsContainerAdapter

        val tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        //tab.text = "Bokmerker"
                        tab.setIcon(R.drawable.ic_baseline_bookmark_24)
                    }
                    1 -> {
                        //tab.text = "Søk"
                        tab.setIcon(R.drawable.ic_baseline_search_24)
                    }
                    2 -> {
                        //tab.text = "Varsler"
                        tab.setIcon(R.drawable.ic_baseline_notifications_24)
                    }
                    3 -> {
                        //tab.text = "Instillinger"
                        tab.setIcon(R.drawable.ic_baseline_settings_24)
                    }
                }
            }

        TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()
    }
}