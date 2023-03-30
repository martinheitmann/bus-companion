package com.app.skyss_companion.view.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.skyss_companion.R
import com.app.skyss_companion.databinding.BookmarkedItemsFragmentBinding
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedItemsFragment : Fragment() {
    val TAG = "BookmarkedItemsFragment"
    //private lateinit var tabLayout: TabLayout
    //private lateinit var tabLayoutAdapter: BookmarkedItemsTabAdapter
    //private lateinit var viewPager: ViewPager2

    //private var _binding: BookmarkedItemsFragmentBinding? = null

    //private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = BookmarkedItemsFragmentBinding.inflate(inflater, container, false)
        //return binding.root

        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SavedElementsView(
                    onNavigateStopGroup = { id -> navigateToStopGroup(id) },
                    onNavigateRouteDirection = { rd -> navigateToRouteDirection(rd) }
                )
            }
        }
    }

    private fun navigateToRouteDirection(bookmarkedRouteDirection: BookmarkedRouteDirection){
        val bundle = Bundle()
        bundle.putString("STOP_IDENTIFIER", bookmarkedRouteDirection.stopGroupIdentifier)
        bundle.putString("ROUTE_DIRECTION_IDENTIFIER", bookmarkedRouteDirection.routeDirectionIdentifier)
        bundle.putString("STOPGROUP_NAME", bookmarkedRouteDirection.stopGroupName)
        bundle.putString("ROUTE_DIRECTION_NAME", bookmarkedRouteDirection.routeDirectionName)
        bundle.putString("LINE_NUMBER", bookmarkedRouteDirection.lineCode)
        findNavController().navigate(R.id.action_tabsContainerFragment_to_routeDirectionTimeTableFragment, bundle)
    }

    private fun navigateToStopPlace(stopIdentifier: String){
        val bundle = Bundle()
        bundle.putString("STOP_IDENTIFIER", stopIdentifier)
        findNavController().navigate(R.id.action_tabsContainerFragment_to_stopPlaceFragment, bundle)
    }

    private fun navigateToStopGroup(stopIdentifier: String){
        val bundle = Bundle()
        bundle.putString("STOP_IDENTIFIER", stopIdentifier)
        findNavController().navigate(R.id.action_tabsContainerFragment_to_stopGroupFragment, bundle)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
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
    }*/
}