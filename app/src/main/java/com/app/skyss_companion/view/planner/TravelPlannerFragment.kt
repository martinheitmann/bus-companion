package com.app.skyss_companion.view.planner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.databinding.FragmentTravelPlannerBinding
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import com.app.skyss_companion.view.planner.location_search.SearchLocationDialogFragment
import com.app.skyss_companion.view.planner.saved_searches.SavedTravelPlansDialog
import dagger.hilt.android.AndroidEntryPoint
import java.time.*

@AndroidEntryPoint
class TravelPlannerFragment : Fragment(),
    SearchLocationDialogFragment.SearchLocationDialogListener,
    SavedTravelPlansDialog.SavedTravelPlansDialogListener {

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

        viewModel.selectedFromFeature.observe(viewLifecycleOwner, ::selectedFromFeatureObserver)
        viewModel.selectedToFeature.observe(viewLifecycleOwner, ::selectedToFeatureObserver)
        viewModel.selectedLocalDateTime.observe(viewLifecycleOwner, ::selectedLocalDateTimeObserver)
        viewModel.selectedTimeType.observe(viewLifecycleOwner, ::selectedTimeTypeObserver)
        binding.travelPlannerTimetypeSwitch.setOnClickListener { viewModel.flipSwitch() }
        binding.travelPlannerDatepickerButton.setOnClickListener { openDatePicker() }

        binding.travelPlannerViewSavedButton.setOnClickListener {
            val bundle = Bundle()
            val fragment = SavedTravelPlansDialog()
            fragment.setListener(this)
            fragment.arguments = bundle
            fragment.show(childFragmentManager, "saved_travel_plans")
        }

        viewModel.mergedFeatures.observe(viewLifecycleOwner) {/* no-op */ }

        viewModel.travelPlans.observe(viewLifecycleOwner) { travelPlans ->
            adapter.setData(travelPlans)
        }

        binding.travelPlannerButtonSave.setOnClickListener {
            viewModel.saveCurrentSearch()
        }

        viewModel.travelPlansLoading.observe(viewLifecycleOwner) { loading ->
            if (loading) binding.travelPlannerPlansListProgressbar.visibility = View.VISIBLE
            else binding.travelPlannerPlansListProgressbar.visibility = View.GONE
        }

        childFragmentManager.setFragmentResultListener(
            FragmentReturnType.DATE.type,
            viewLifecycleOwner,
            ::setDateAndOpenTimePicker
        )

        childFragmentManager.setFragmentResultListener(
            FragmentReturnType.TIME.type,
            viewLifecycleOwner,
            ::setTime
        )
    }

    private fun selectedTimeTypeObserver(timeType: Boolean) {
        binding.travelPlannerTimetypeSwitch.isChecked = timeType
        if (!timeType) {
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

    private fun selectedFromFeatureObserver(feature: GeocodingFeature?) {
        feature?.let { f ->
            binding.travelPlannerStart.text = f.properties.label
        }
    }

    private fun selectedToFeatureObserver(feature: GeocodingFeature?) {
        feature?.let { f ->
            binding.travelPlannerDest.text = f.properties.label
        }
    }

    private fun selectedLocalDateTimeObserver(localDateTime: LocalDateTime) {
        binding.travelPlannerDateTextview.text = viewModel.formatDate(localDateTime)
    }

    private fun toggleDialog(type: String) {
        val bundle = Bundle()
        bundle.putString("type", type)

        val searchFragment = SearchLocationDialogFragment()
        searchFragment.setListener(this)
        searchFragment.arguments = bundle
        searchFragment.show(childFragmentManager, "search")
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

    private fun openDatePicker() {
        val datePickerFragment = TravelPlannerDatePickerFragment()
        datePickerFragment.show(childFragmentManager, "travelPlannerDatePicker")
    }

    private fun setDateAndOpenTimePicker(requestKey: String, bundle: Bundle) {
        val year = bundle.getInt("year")
        val month = bundle.getInt("month")
        val day = bundle.getInt("day")
        Log.d(tag, "setDateAndOpenTimePicker called with year,month,day = $year,$month,$day")
        val partialDate = LocalDateTime
            .now()
            .withYear(year)
            .withMonth(month)
            .withDayOfMonth(day)
        val timePickerFragment = TravelPlannerTimePickerFragment(partialDate)
        timePickerFragment.show(childFragmentManager, "travelPlannerTimePicker")
    }

    private fun setTime(requestKey: String, bundle: Bundle) {
        val hour = bundle.getInt("hour")
        val minute = bundle.getInt("minute")
        val fullDateAsString = bundle.getString("fullDate")
        val fullDateAndTime = LocalDateTime.parse(fullDateAsString)
        Log.d(tag, "setTime called with hour,minute = $hour,$minute")
        viewModel.selectedLocalDateTime.value?.let { localDateTime ->
            viewModel.selectedLocalDateTime.postValue(fullDateAndTime)
        }
    }

    // Dialog fragment listener callbacks

    override fun onItemSelected(feature: GeocodingFeature, type: String) {
        when (type) {
            "start" -> viewModel.setFromFeature(feature)
            "dest" -> viewModel.setToFeature(feature)
        }
    }

    override fun onDialogClosed(dialog: DialogFragment) {
        // Cleanup if needed from dialog fragment.
    }

    override fun onSavedTravelPlansItemSelected(element: BookmarkedTravelPlan) {
        viewModel.selectedFromFeature.postValue(element.fromFeature)
        viewModel.selectedToFeature.postValue(element.toFeature)
    }

    override fun onSavedTravelPlansClosed(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

}