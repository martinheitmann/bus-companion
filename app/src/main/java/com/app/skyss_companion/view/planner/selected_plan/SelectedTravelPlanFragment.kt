package com.app.skyss_companion.view.planner.selected_plan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.FragmentSelectedTravelPlanBinding
import com.app.skyss_companion.view.stop_place.StopGroupScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectedTravelPlanFragment : Fragment() {


    private val mTag = "TravelPlannerFragment"

    /*private val viewModel: SelectedTravelPlanViewModel by viewModels()
    private var _binding: FragmentSelectedTravelPlanBinding? = null

    private lateinit var adapter: SelectedTravelPlanAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private val binding get() = _binding!!*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = this.arguments
        val identifier = bundle?.getString("travelPlanId") ?: ""
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TravelPlanScreen(identifier, ::onBack, ::onBookmark)
            }
        }
    }

    private fun onBookmark() {}

    private fun onBack() {
        findNavController().popBackStack()
    }


    /* override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         _binding = FragmentSelectedTravelPlanBinding.inflate(inflater, container, false)
         return binding.root
     }*/

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SelectedTravelPlanAdapter(requireContext())
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.selectedTravelPlanRecyclerview
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        binding.selectedTravelPlanBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.selectedTravelPlan.observe(viewLifecycleOwner) { travelPlan ->
            adapter.setData(travelPlan.travelSteps, travelPlan.end)
            viewModel.getDateString(travelPlan)?.let { str: String ->
                binding.selectedTravelPlanDateTime.text = str
            }
            viewModel.getDurationString(travelPlan)?.let { str ->
                binding.selectedTravelPlanDuration.text = str
            }
        }
        val bundle = this.arguments
        bundle?.getString("travelPlanId")?.let { id ->
            viewModel.fetchTravelPlan(id)
        } ?: run {
            Log.d(
                mTag,
                "WARNING bundle argument 'travelPlanId' was null, cannot request travel plan. Contents were: " + bundle.toString(),
            )
        }
    }*/
}