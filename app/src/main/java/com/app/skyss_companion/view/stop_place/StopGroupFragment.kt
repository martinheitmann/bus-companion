package com.app.skyss_companion.view.stop_place

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.app.skyss_companion.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopGroupFragment : Fragment() {
    val fragmentTag = "StopGroupFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val identifier = arguments?.getString("STOP_IDENTIFIER")
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StopGroupScreen(
                    identifier = identifier ?: "",
                    onBackTapped = ::navigateBack,
                    onBookmarkTapped = ::toggleBookmark,
                    onRouteDirectionTapped = ::navigateToTimeTable
                )
            }
        }
    }

    private fun navigateBack(){
        findNavController().popBackStack()
    }

    private fun navigateToTimeTable(stopIdentifier: String?, routeDirectionIdentifier: String?, stopName: String?, directionName: String?, lineNumber: String?){
        Log.d(fragmentTag, "navigating to time table with $stopIdentifier, $routeDirectionIdentifier, $stopName, $directionName, $lineNumber")
        if(stopIdentifier != null && routeDirectionIdentifier != null){
            val bundle = Bundle()
            bundle.putString("STOP_IDENTIFIER", stopIdentifier)
            bundle.putString("ROUTE_DIRECTION_IDENTIFIER", routeDirectionIdentifier)
            bundle.putString("STOPGROUP_NAME", stopName)
            bundle.putString("ROUTE_DIRECTION_NAME", directionName)
            bundle.putString("LINE_NUMBER", lineNumber)
            findNavController().navigate(R.id.action_stopGroupFragment_to_timeTableComposeFragment, bundle)
        }
    }

    private fun toggleBookmark(){

    }
}