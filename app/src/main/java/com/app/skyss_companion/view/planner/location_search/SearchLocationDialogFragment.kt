package com.app.skyss_companion.view.planner.location_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.view.planner.TravelPlannerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchLocationDialogFragment : DialogFragment() {

    private val mTag = "SearchLocationDialogFragment"

    private val viewModel: TravelPlannerViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LocationSearchListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var searchTextEdit: EditText
    private lateinit var searchErrorTextView: TextView
    private lateinit var fetchLoadingIndicator: ProgressBar
    private lateinit var type: String

    private lateinit var listener: SearchLocationDialogListener

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

        adapter = LocationSearchListAdapter { pos -> select(pos) }
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView = view.findViewById(R.id.dialog_search_recyclerview)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        searchTextEdit = view.findViewById(R.id.dialog_search_bar)
        searchErrorTextView = view.findViewById(R.id.dialog_search_error)
        fetchLoadingIndicator = view.findViewById(R.id.dialog_search_loading)

        searchTextEdit.addTextChangedListener(
            // params: text, start, before, count
            onTextChanged = { str, _, _, _ ->
                if (str?.length != null && str.length > 3) {
                    Log.d(mTag, "search term entered '${str}'")
                    viewModel.geocodeAutocomplete(str.toString())
                }
            }
        )

        viewModel.fetchFeaturesError.observe(viewLifecycleOwner) {
            if (it != null) {
                searchErrorTextView.text = it
                searchErrorTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                searchErrorTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.features.observe(viewLifecycleOwner) { data ->
            adapter.setData(data.map { d -> d.properties })
        }

        viewModel.fetchFeaturesLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                recyclerView.visibility = View.GONE
                fetchLoadingIndicator.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                fetchLoadingIndicator.visibility = View.GONE
            }
        }

        return view
    }

    private fun closeDialog() {
        listener.onDialogClosed(this)
        dismiss()
    }

    fun select(pos: Int) {
        val items = viewModel.features.value
        if(items != null && items.isNotEmpty()){
            val item = items[pos]
            listener.onItemSelected(item, type)
            closeDialog()
        }
    }
}