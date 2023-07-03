package com.app.skyss_companion.view.routedirection_timetable

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
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
import com.app.skyss_companion.view.stop_place.StopGroupScreen

import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.ZonedDateTime

@AndroidEntryPoint
class TimeTableComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val stopGroupIdentifier = arguments?.getString("STOP_IDENTIFIER") ?: ""
        val routeDirectionIdentifier = arguments?.getString("ROUTE_DIRECTION_IDENTIFIER") ?: ""
        val stopGroupName = arguments?.getString("STOPGROUP_NAME") ?: ""
        val routeDirectionName = arguments?.getString("ROUTE_DIRECTION_NAME") ?: ""
        val lineCode = arguments?.getString("LINE_NUMBER") ?: ""
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RouteDirectionTimeTableScreen(
                    stopGroupIdentifier = stopGroupIdentifier,
                    routeDirectionIdentifier = routeDirectionIdentifier,
                    stopGroupName = stopGroupName,
                    routeDirectionName = routeDirectionName,
                    lineCode = lineCode
                )
            }
        }
    }
}