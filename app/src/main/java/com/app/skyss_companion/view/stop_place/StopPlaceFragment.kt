package com.app.skyss_companion.view.stop_place

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.StopPlaceFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import com.app.skyss_companion.R

@AndroidEntryPoint
class StopPlaceFragment : Fragment() {
    val TAG = "SearchStopsFragment"
    private val viewModel: StopPlaceViewModel by viewModels()
    private lateinit var adapter: StopPlaceAdapter
    private lateinit var lineCodesAdapter: StopPlaceLineCodeAdapter
    private lateinit var lineCodesFilterAdapter: StopPlaceFilterLineCodeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var lineCodesRecyclerView: RecyclerView
    private lateinit var lineCodesLayoutManager: LinearLayoutManager
    private lateinit var lineCodesFilterLayoutManager: LinearLayoutManager
    private lateinit var lineCodesFilterRecyclerView: RecyclerView
    private var _binding: StopPlaceFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StopPlaceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = StopPlaceAdapter(requireContext(), ::navigateToTimeTable)
        lineCodesAdapter = StopPlaceLineCodeAdapter(requireContext(), onTap = {index -> viewModel.addLineCodeToFilter(index)})
        lineCodesFilterAdapter = StopPlaceFilterLineCodeAdapter(requireContext(), onTap = {index -> viewModel.removeLineCodeFromFilter(index)})
        recyclerView = binding.stopPlaceRecyclerview
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        lineCodesRecyclerView = binding.stopPlaceLinecodesRecyclerview
        lineCodesFilterRecyclerView = binding.stopPlaceRecyclerviewLinecodeFilter
        val identifier = arguments?.getString("STOP_IDENTIFIER")

        lineCodesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lineCodesFilterLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        lineCodesRecyclerView.apply {
            layoutManager = lineCodesLayoutManager
            adapter = lineCodesAdapter
        }

        lineCodesFilterRecyclerView.apply {
            layoutManager = lineCodesFilterLayoutManager
            adapter = lineCodesFilterAdapter
        }

        if(identifier != null){
            binding.stopPlaceImagebuttonBookmarkActive.setOnClickListener { viewModel.removeFavorited(identifier) }
            binding.stopPlaceImagebuttonBookmarkInactive.setOnClickListener { viewModel.addFavorited(identifier) }
            viewModel.checkIsFavorited(identifier)
            viewModel.fetchStopPlace(identifier)
            viewModel.stopGroup.observe(viewLifecycleOwner, { stopGroup ->
                binding.stopPlaceTetxviewName.text = stopGroup.description
            })
            viewModel.filteredStopPlaceListItems.observe(viewLifecycleOwner, { listItems ->
                adapter.setData(listItems)
            })
            viewModel.lineCodeFilter.observe(viewLifecycleOwner, { filter ->
                if(filter.isEmpty()) lineCodesFilterRecyclerView.visibility = View.GONE
                else lineCodesFilterRecyclerView.visibility = View.VISIBLE
                lineCodesFilterAdapter.setData(filter)
            })
            viewModel.lineCodes.observe(viewLifecycleOwner, { lineCodes ->
                lineCodesAdapter.setData(lineCodes)
            })
        }

        viewModel.isLoading.observe(viewLifecycleOwner, { value ->
            if(value){
                binding.progressBar.visibility = View.VISIBLE
                binding.stopPlaceTetxviewName.visibility = View.GONE
            }
            if(!value){
                binding.progressBar.visibility = View.GONE
                binding.stopPlaceTetxviewName.visibility = View.VISIBLE
            }

        })

        viewModel.isBookmarked.observe(viewLifecycleOwner, { value ->
            if(value){
                binding.stopPlaceImagebuttonBookmarkActive.visibility = View.VISIBLE
                binding.stopPlaceImagebuttonBookmarkInactive.visibility = View.GONE
            } else {
                binding.stopPlaceImagebuttonBookmarkActive.visibility = View.GONE
                binding.stopPlaceImagebuttonBookmarkInactive.visibility = View.VISIBLE
            }
        })

        binding.stopPlaceBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.stopPlaceImagebuttonRefresh.setOnClickListener {
            identifier?.let { viewModel.fetchStopPlace(it) }
        }
    }

    private fun navigateToTimeTable(stopIdentifier: String?, routeDirectionIdentifier: String?, stopName: String?, directionName: String?, lineNumber: String?){
        if(stopIdentifier != null && routeDirectionIdentifier != null){
            val bundle = Bundle()
            bundle.putString("STOP_IDENTIFIER", stopIdentifier)
            bundle.putString("ROUTE_DIRECTION_IDENTIFIER", routeDirectionIdentifier)
            bundle.putString("STOPGROUP_NAME", stopName)
            bundle.putString("ROUTE_DIRECTION_NAME", directionName)
            bundle.putString("LINE_NUMBER", lineNumber)
            findNavController().navigate(R.id.action_stopPlaceFragment_to_routeDirectionTimeTableFragment, bundle)
        }
    }

}