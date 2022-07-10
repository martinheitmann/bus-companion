package com.app.skyss_companion.view.planner.location_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.view.planner.TravelPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify


@AndroidEntryPoint
class SearchLocationDialogFragment : DialogFragment() {

    private val mTag = "SearchLocDialogFrag"

    private val viewModel: TravelPlannerViewModel by viewModels()

    private lateinit var searchTextEdit: EditText
    private lateinit var searchErrorTextView: TextView
    private lateinit var fetchLoadingIndicator: ProgressBar
    private lateinit var type: String

    private lateinit var listener: SearchLocationDialogListener

    // Components for showing last used.
    private val lastUsedListAdapter = LocationSearchListAdapter { selectLastUsed(it) }

    // Components for showing current search
    private lateinit var currentSearchRecyclerView: RecyclerView
    private val currentSearchListAdapter = LocationSearchListAdapter { pos -> select(pos) }
    private lateinit var currentSearchLayoutManager: LinearLayoutManager
    private var currentAdapter: Int? = null

    interface SearchLocationDialogListener {
        fun onItemSelected(feature: GeocodingFeature, type: String)
        fun onDialogClosed(dialog: DialogFragment)
    }

    fun setListener(context: SearchLocationDialogListener) {
        this.listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_search_location, container, false)
        val arguments = this.arguments
        type = arguments?.getString("type") ?: ""
        assignSearchViewComponents(view)
        setSearchRecyclerViewLayoutManager()
        searchTextEdit.addTextChangedListener(onTextChanged = ::searchListener)
        viewModel.fetchFeaturesError.observe(viewLifecycleOwner, ::onFetchFeatureError)
        viewModel.features.observe(viewLifecycleOwner, ::onFeaturesTrigger)
        viewModel.fetchFeaturesLoading.observe(viewLifecycleOwner, ::onFeaturesLoading)
        viewModel.lastUsedLocations.observe(viewLifecycleOwner) { data ->
            Log.d(mTag, "viewModel lastUsedLocations triggered with ${data.size} items.")
            lastUsedListAdapter.setData(data.map { d -> d.properties })
            initViewState()
        }
        fetchLastUsed()
        return view
    }

    private fun closeDialog() {
        listener.onDialogClosed(this)
        dismiss()
    }

    fun select(pos: Int) {
        val items = viewModel.features.value
        if (items != null && items.isNotEmpty()) {
            val item = items[pos]
            listener.onItemSelected(item, type)
            closeDialog()
        }
    }

    private fun selectLastUsed(pos: Int) {
        val items = viewModel.lastUsedLocations.value
        if (items != null && items.isNotEmpty()) {
            val item = items[pos]
            listener.onItemSelected(item, type)
            closeDialog()
        }
    }

    private fun searchListener(text: CharSequence?, start: Int, before: Int, count: Int) {
        when (text?.length) {
            0, 1, 2 -> setLastUsedLocationsView()
            null -> {
                /* no-op */
            }
            else -> setGeocodeSearchView(text)
        }
    }

    private fun setGeocodeSearchView(text: CharSequence?) {
        Log.d(mTag, "search term entered '${text}'")
        if (currentAdapter != 1) {
            Log.d(mTag, "setGeocodeSearchView adapter was $currentAdapter")
            lastUsedListAdapter.clear()
            currentSearchRecyclerView.adapter = currentSearchListAdapter
            currentAdapter = 1
            Log.d(mTag, "setGeocodeSearchView setting current adapter id to 1.")
        }
        viewModel.geocodeAutocomplete(text.toString())
    }

    private fun setLastUsedLocationsView() {
        if (currentAdapter != 0) {
            Log.d(
                mTag,
                "setLastUsedLocationsView adapter was $currentAdapter, setting layout and data."
            )
            viewModel.cancelGeocode()
            //currentSearchListAdapter.clear()
            //currentSearchRecyclerView.removeAllViews()
            currentAdapter = 0
            Log.d(mTag, "setLastUsedLocationsView setting current adapter id to 0.")
            currentSearchRecyclerView.adapter = lastUsedListAdapter
            lastUsedListAdapter.setData(viewModel.lastUsedLocations.value?.map { v -> v.properties }
                ?: emptyList())
            Log.d(
                mTag,
                "setLastUsedLocationsView set adapter had ${lastUsedListAdapter.dataSet.size} items."
            )
        } else {
            Log.d(mTag, "setLastUsedLocationsView adapter id was 0, skipping.")
        }
    }

    private fun initViewState() {
        setLastUsedLocationsView()
    }

    private fun onFeaturesLoading(isLoading: Boolean) {
        if (isLoading) {
            currentSearchRecyclerView.visibility = View.GONE
            fetchLoadingIndicator.visibility = View.VISIBLE
        } else {
            currentSearchRecyclerView.visibility = View.VISIBLE
            fetchLoadingIndicator.visibility = View.GONE
        }
    }

    private fun onFeaturesTrigger(data: List<GeocodingFeature>) {
        currentSearchListAdapter.setData(data.map { d -> d.properties })
    }

    private fun onFetchFeatureError(error: String?) {
        if (error != null) {
            searchErrorTextView.text = error
            searchErrorTextView.visibility = View.VISIBLE
            currentSearchRecyclerView.visibility = View.GONE
        } else {
            searchErrorTextView.visibility = View.GONE
            currentSearchRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun fetchLastUsed() {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getLastUsedLocations()
        }
    }

    private fun assignSearchViewComponents(view: View) {
        searchTextEdit = view.findViewById(R.id.dialog_search_bar)
        searchErrorTextView = view.findViewById(R.id.dialog_search_error)
        fetchLoadingIndicator = view.findViewById(R.id.dialog_search_loading)
        currentSearchRecyclerView = view.findViewById(R.id.dialog_search_recyclerview)
    }

    private fun setSearchRecyclerViewLayoutManager() {
        currentSearchLayoutManager = LinearLayoutManager(requireContext())
        currentSearchRecyclerView.layoutManager = currentSearchLayoutManager
    }

}