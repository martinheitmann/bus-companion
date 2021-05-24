package com.app.skyss_companion.view.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.app.skyss_companion.databinding.BookmarkedItemsFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedItemsFragment : Fragment() {
    val TAG = "BookmarkedItemsFragment"
    private lateinit var tabLayout: TabLayout
    private lateinit var tabLayoutAdapter: BookmarkedItemsTabAdapter
    private lateinit var viewPager: ViewPager2

    private var _binding: BookmarkedItemsFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BookmarkedItemsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tabLayoutAdapter = BookmarkedItemsTabAdapter(this)
        tabLayout = binding.bookmarkedItemsTablayout
        viewPager = binding.bookmarkedItemsViewpager

        viewPager.adapter = tabLayoutAdapter

        val tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> { tab.text = "Holdeplasser" }
                    1 -> { tab.text = "Linjer" }
                }
            }

        TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()
    }
}