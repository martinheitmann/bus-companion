package com.app.skyss_companion.view.routedirection_timetable

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.RouteDirectionTimeTableFragmentBinding
import com.app.skyss_companion.misc.AlarmUtils
import com.app.skyss_companion.misc.DateUtils
import com.app.skyss_companion.misc.NotificationUtils
import com.app.skyss_companion.model.PassingTimeAlert

import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.ZonedDateTime

@AndroidEntryPoint
class RouteDirectionTimeTableFragment : Fragment(), SetAlertDialogFragment.SetAlertDialogListener {
    val TAG = "RouteDirTTableFragment"
    private val viewModel: RouteDirectionTimeTableViewModel by viewModels()

    private lateinit var passingTimeDayTabsAdapter: PassingTimeDaysTabAdapter
    private lateinit var passingTimeDayTabsRecyclerView: RecyclerView
    private lateinit var passingTimeDayTabsLinearLayoutManager: LinearLayoutManager

    private lateinit var passingTimeListAdapter: PassingTimeListAdapter
    private lateinit var passingTimeListItemsRecyclerView: RecyclerView
    private lateinit var passingTimeListItemsLinearLayoutManager: GridLayoutManager

    private var _binding: RouteDirectionTimeTableFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RouteDirectionTimeTableFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val stopIdentifier = arguments?.getString("STOP_IDENTIFIER") ?: ""
        val routeDirectionIdentifier = arguments?.getString("ROUTE_DIRECTION_IDENTIFIER") ?: ""
        val stopName = arguments?.getString("STOPGROUP_NAME") ?: ""
        val directionName = arguments?.getString("ROUTE_DIRECTION_NAME") ?: ""
        val lineNumber = arguments?.getString("LINE_NUMBER") ?: ""

        binding.timeTableRouteDirectionName.text = directionName
        binding.timeTableStopPlaceName.text = stopName
        binding.timeTableRouteDirectionLinecode.text = lineNumber

        passingTimeDayTabsAdapter =
            PassingTimeDaysTabAdapter(this.requireContext(), ::onDaysTabTapped)
        passingTimeDayTabsRecyclerView = binding.timeTableRouteDirectionFilterRecyclerview
        passingTimeDayTabsLinearLayoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        passingTimeDayTabsRecyclerView.adapter = passingTimeDayTabsAdapter
        passingTimeDayTabsRecyclerView.layoutManager = passingTimeDayTabsLinearLayoutManager

        passingTimeListAdapter = PassingTimeListAdapter(this.requireContext(), ::mOnTap)
        passingTimeListItemsRecyclerView = binding.timeTableRouteDirectionRecyclerview
        passingTimeListItemsLinearLayoutManager = GridLayoutManager(this.requireContext(), 4)
        passingTimeListItemsRecyclerView.adapter = passingTimeListAdapter
        passingTimeListItemsRecyclerView.layoutManager = passingTimeListItemsLinearLayoutManager

        viewModel.passingTimeListItems.observe(viewLifecycleOwner, {
            Log.d(TAG, "Fragment passingTimeListItems observer triggered. ${it.size} items: ${it}.")
            passingTimeListAdapter.setData(it)
        })

        viewModel.checkIsBookmarked(stopIdentifier, routeDirectionIdentifier)
        viewModel.isBookmarked.observe(viewLifecycleOwner, { isBookmarked ->
            if (isBookmarked != null && isBookmarked) {
                binding.timeTableBookmarkActive.visibility = View.VISIBLE
                binding.timeTableBookmarkInactive.visibility = View.GONE
            }
            if (isBookmarked != null && !isBookmarked) {
                binding.timeTableBookmarkActive.visibility = View.GONE
                binding.timeTableBookmarkInactive.visibility = View.VISIBLE
            }
        })

        binding.timeTableBookmarkActive.setOnClickListener {
            viewModel.removeBookmark(stopIdentifier, routeDirectionIdentifier)
        }

        binding.timeTableBookmarkInactive.setOnClickListener {
            viewModel.bookmark(
                routeDirectionIdentifier,
                stopIdentifier,
                directionName,
                stopName,
                lineNumber
            )
        }

        binding.timeTableBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.timeTableStopPlaceName.setOnClickListener {
            // findNavController().popBackStack()
        }

        binding.timeTableRefreshButton.setOnClickListener {
            viewModel.fetchTimeTables(stopIdentifier, routeDirectionIdentifier)
        }

        viewModel.passingTimeDayTabs.observe(viewLifecycleOwner, {
            Log.d(TAG, "Fragment passingTimeDayTabs observer triggered. ${it.size} items: ${it}.")
            passingTimeDayTabsAdapter.setData(it)
            viewModel.setSelectedDayTab(it)
        })

        viewModel.fetchTimeTables(stopIdentifier, routeDirectionIdentifier)
    }

    private fun onDaysTabTapped(num: Int) {
        Log.d(TAG, "onDaysTabTapped num = $num")
        viewModel.markSelected(num)
    }

    private fun mOnTap(num: Int) {
        Log.d(TAG, "mOnTap num = $num")
        val passingTime = viewModel.passingTimeListItems.value?.get(num)
        passingTime?.let {
            val newFragment = SetAlertDialogFragment(passingTime)
            newFragment.setListener(this)
            newFragment.show(childFragmentManager, "alerts")
        } ?: run {
            Log.d(TAG, "Showing dialog failed.")
        }
    }

    override fun onDialogPositiveClick(
        passingTimeListItem: PassingTimeListItem,
        inputMinutes: Int
    ) {
        viewModel.setAlert(passingTimeListItem, inputMinutes, arguments)
        showSuccessToast(requireContext())
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Can be implemented if you want to perform some action
        // upon dialog dismiss.
    }

    private fun showSuccessToast(context: Context){
        val text = "Varsel opprettet!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

}