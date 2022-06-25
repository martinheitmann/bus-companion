package com.app.skyss_companion.view.planner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.databinding.FragmentTravelPlannerBinding
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.view.planner.location_search.SearchLocationDialogFragment
import com.app.skyss_companion.view.routedirection_timetable.SetAlertDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelPlannerFragment : Fragment(),
    SearchLocationDialogFragment.SearchLocationDialogListener {

    private val mTag = "TravelPlannerFragment"
    private val viewModel: TravelPlannerViewModel by viewModels()
    private var _binding: FragmentTravelPlannerBinding? = null

    private lateinit var adapter: TravelPlannerListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(mTag, "onViewCreated")

        adapter = TravelPlannerListAdapter(requireContext()) { pos ->
            navigateToSelectedTravelPlan(pos)
        }
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.travelPlannerPlansList
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        binding.travelPlannerStart.setOnClickListener { toggleDialog("start") }
        binding.travelPlannerDest.setOnClickListener { toggleDialog("dest") }

        viewModel.selectedFromFeature.observe(viewLifecycleOwner) { feature ->
            feature?.let { f ->
                binding.travelPlannerStart.text = f.properties.label
            }
        }

        viewModel.selectedToFeature.observe(viewLifecycleOwner) { feature ->
            feature?.let { f ->
                binding.travelPlannerDest.text = f.properties.label
            }
        }

        viewModel.selectedLocalDateTime.observe(viewLifecycleOwner) { ldt ->
            binding.travelPlannerDateTextview.text = viewModel.formatDate(ldt)
        }

        viewModel.selectedTimeType.observe(viewLifecycleOwner) { tt ->
            binding.travelPlannerTimetypeSwitch.isChecked = tt
            if (!tt) {
                binding.travelPlannerDepartureLabel.setTextColor(R.attr.colorPrimary)
                binding.travelPlannerArrivalLabel.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.cardview_dark_background
                    )
                )
            } else {
                binding.travelPlannerDepartureLabel.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.cardview_dark_background
                    )
                )
                binding.travelPlannerArrivalLabel.setTextColor(R.attr.colorPrimary)
            }
        }

        binding.travelPlannerTimetypeSwitch.setOnClickListener {
            viewModel.flipSwitch()
        }

        viewModel.mergedFeatures.observe(viewLifecycleOwner) {
            /* no-op */
            /* MediatorLiveData requires a listener in order to remain active.  */
        }

        viewModel.travelPlans.observe(viewLifecycleOwner) { travelPlans ->
            adapter.setData(travelPlans)
        }

        viewModel.travelPlansLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) binding.travelPlannerPlansListProgressbar.visibility = View.VISIBLE
            else binding.travelPlannerPlansListProgressbar.visibility = View.GONE
        }
    }

    private fun toggleDialog(type: String) {
        val bundle = Bundle()
        bundle.putString("type", type)

        val searchFragment = SearchLocationDialogFragment()
        searchFragment.setListener(this)
        searchFragment.arguments = bundle
        searchFragment.show(childFragmentManager, "search")
    }

    override fun onItemSelected(feature: GeocodingFeature, type: String) {
        when (type) {
            "start" -> viewModel.setFromFeature(feature)
            "dest" -> viewModel.setToFeature(feature)
        }
    }

    override fun onDialogClosed(dialog: DialogFragment) {
        // Cleanup if needed from dialog fragment.
    }

    private fun navigateToSelectedTravelPlan(pos: Int) {
        val travelPlan = viewModel.travelPlans.value?.get(pos)
        travelPlan?.let { tp ->
            val id = tp.id
            if (id != null) {
                val bundle = Bundle()
                bundle.putString("travelPlanId", id)
                findNavController().navigate(
                    R.id.action_tabsContainerFragment_to_selectedTravelPlanFragment,
                    bundle
                )
            }
        }
    }

}