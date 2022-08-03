package com.app.skyss_companion.view.planner.saved_searches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.BookmarkedTravelPlan
import com.app.skyss_companion.view.planner.TravelPlannerViewModel
import com.app.skyss_companion.view.planner.location_search.SavedTravelPlansListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SavedTravelPlansDialog : DialogFragment() {

    private val mTag = "SavedTPDialog"

    private val viewModel: TravelPlannerViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedTravelPlansListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var type: String
    private lateinit var emptyListText: TextView
    private lateinit var backButton: ImageButton

    private lateinit var listener: SavedTravelPlansDialogListener

    interface SavedTravelPlansDialogListener {
        fun onSavedTravelPlansItemSelected(element: BookmarkedTravelPlan)
        fun onSavedTravelPlansClosed(dialog: DialogFragment)
    }

    fun setListener(context: SavedTravelPlansDialogListener) {
        this.listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_saved_travel_plans, container, false)
        val arguments = this.arguments
        type = arguments?.getString("type") ?: ""
        adapter = SavedTravelPlansListAdapter(::select, ::delete)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView = view.findViewById(R.id.dialog_saved_travel_plans_recyclerview)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        emptyListText = view.findViewById(R.id.dialog_saved_travel_plans_empty_list_tw)
        viewModel.savedTravelPlans.observe(viewLifecycleOwner, ::onSavedTravelPlansListenerTriggered)

        backButton = view.findViewById(R.id.dialog_saved_travel_plans_back)
        backButton.setOnClickListener { closeDialog() }

        return view
    }

    private fun onSavedTravelPlansListenerTriggered(data: List<BookmarkedTravelPlan>){
        if (data.isEmpty()) emptyListText.visibility = View.VISIBLE
        else emptyListText.visibility = View.GONE
        adapter.setData(data)
    }

    private fun closeDialog() {
        dismiss()
    }

    fun delete(pos: Int){
        viewModel.deleteSavedTravelPlan(pos)
    }

    fun select(pos: Int) {
        viewModel.savedTravelPlans.value?.let { bookmarks ->
            val bookmark = bookmarks[pos]
            listener.onSavedTravelPlansItemSelected(bookmark)
            dismiss()
        }
    }
}