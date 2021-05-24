package com.app.skyss_companion.view.bookmark.routedirections

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.databinding.BookmarkedRouteDirectionsListFragmentBinding
import com.app.skyss_companion.misc.StopPlaceUtils
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.view.bookmark.BookmarkedRouteDirectionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedRouteDirectionsListFragment : Fragment() {

    private val viewModel: BookmarkedRouteDirectionsListViewModel by viewModels()
    private lateinit var bookmarkedRouteDirectionsAdapter: BookmarkedRouteDirectionsAdapter
    private lateinit var bookmarkedRouteDirectionsRecyclerView: RecyclerView
    private lateinit var bookmarkedRouteDirectionsLayoutManager: LinearLayoutManager

    private var _binding:  BookmarkedRouteDirectionsListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  BookmarkedRouteDirectionsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookmarkedRouteDirectionsAdapter = BookmarkedRouteDirectionsAdapter(onItemTapped = { r: BookmarkedRouteDirection -> navigateToRouteDirection(r)} )
        bookmarkedRouteDirectionsRecyclerView = binding.bookmarkedRouteDirectionsRecyclerview
        bookmarkedRouteDirectionsLayoutManager = LinearLayoutManager(requireContext())
        bookmarkedRouteDirectionsRecyclerView.adapter = bookmarkedRouteDirectionsAdapter
        bookmarkedRouteDirectionsRecyclerView.layoutManager = bookmarkedRouteDirectionsLayoutManager

        viewModel.bookmarkedRouteDirections.observe(viewLifecycleOwner, { routeDirections ->
            Log.d(StopPlaceUtils.TAG, "Bookmarked StopGroups fetched: ${routeDirections.size}")
            bookmarkedRouteDirectionsAdapter.setData(routeDirections)
        })
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

}