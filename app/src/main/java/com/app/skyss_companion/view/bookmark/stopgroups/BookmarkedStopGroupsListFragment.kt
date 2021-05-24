package com.app.skyss_companion.view.bookmark.stopgroups

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
import com.app.skyss_companion.databinding.BookmarkedStopGroupsListFragmentBinding
import com.app.skyss_companion.misc.StopPlaceUtils.Companion.TAG
import com.app.skyss_companion.view.bookmark.BookmarkedStopGroupsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkedStopGroupsListFragment : Fragment() {

    private val viewModel: BookmarkedStopGroupsListViewModel by viewModels()

    private lateinit var bookmarkedStopGroupsAdapter: BookmarkedStopGroupsAdapter
    private lateinit var bookmarkedStopGroupsRecyclerView: RecyclerView
    private lateinit var bookmarkedStopGroupsLayoutManager: LinearLayoutManager

    private var _binding: BookmarkedStopGroupsListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BookmarkedStopGroupsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookmarkedStopGroupsAdapter = BookmarkedStopGroupsAdapter(onItemTapped = { s: String -> navigateToStopPlace(s)} )
        bookmarkedStopGroupsRecyclerView = binding.bookmarkedStopGroupsRecyclerview
        bookmarkedStopGroupsLayoutManager = LinearLayoutManager(requireContext())
        bookmarkedStopGroupsRecyclerView.adapter = bookmarkedStopGroupsAdapter
        bookmarkedStopGroupsRecyclerView.layoutManager = bookmarkedStopGroupsLayoutManager

        viewModel.bookmarkedStopGroups.observe(viewLifecycleOwner, { favorites ->
            Log.d(TAG, "Bookmarked StopGroups fetched: ${favorites.size}")
            bookmarkedStopGroupsAdapter.setData(favorites)
        })
    }

    private fun navigateToStopPlace(stopIdentifier: String){
        val bundle = Bundle()
        bundle.putString("STOP_IDENTIFIER", stopIdentifier)
        findNavController().navigate(R.id.action_tabsContainerFragment_to_stopPlaceFragment, bundle)
    }

}