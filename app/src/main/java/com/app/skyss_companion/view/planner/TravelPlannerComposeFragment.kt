package com.app.skyss_companion.view.planner

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import com.app.skyss_companion.R
import com.app.skyss_companion.model.travelplanner.TravelPlan
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelPlannerComposeFragment : Fragment() {
    val classTag = "FRAG-054272"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TravelPlannerScreen(::navigateToTravelPlan)
            }
        }
    }

    private fun navigateToTravelPlan(t: TravelPlan){
        val bundle = Bundle()
        bundle.putString("travel_plan_id", t.id)
        bundle.putString("travel_plan_url", t.url)
        Log.d(classTag, "[navigateToTravelPlan] navigating to travel plan with id ${t.id} and url ${t.url}.")
        findNavController().navigate(R.id.action_tabsContainerFragment_to_selectedTravelPlanFragment, bundle)
    }
}