package com.app.skyss_companion.view.stop_place

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.databinding.SearchStopsFragmentBinding
import com.app.skyss_companion.databinding.StopPlaceFragmentBinding
import com.app.skyss_companion.misc.StopPlaceUtils
import com.app.skyss_companion.view.search.SearchStopsViewModel
import com.app.skyss_companion.view.search.SearchViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopPlaceFragment : Fragment() {
    val TAG = "SearchStopsFragment"
    private val viewModel: StopPlaceViewModel by viewModels()
    private lateinit var adapter: StopPlaceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var _binding: StopPlaceFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StopPlaceFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = StopPlaceAdapter(requireContext())
        recyclerView = binding.stopPlaceRecyclerview
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val identifier = arguments?.getString("STOP_IDENTIFIER")

        if(identifier != null){
            binding.stopPlaceImagebuttonFavorited.setOnClickListener { viewModel.removeFavorited(identifier) }
            binding.stopPlaceImagebuttonNotFavorited.setOnClickListener { viewModel.addFavorited(identifier) }
            viewModel.checkIsFavorited(identifier)
            viewModel.fetchStopPlace(identifier)
            viewModel.stopGroup.observe(viewLifecycleOwner, { sg ->
                binding.stopPlaceTetxviewName.text = sg.description

                val stops = sg.stops
                if(stops != null){
                    val data = StopPlaceUtils.createListData(stops)
                    adapter.setData(data)
                }
            })
        }

        viewModel.isFavorited.observe(viewLifecycleOwner, { value ->
            if(value){
                binding.stopPlaceImagebuttonFavorited.visibility = View.VISIBLE
                binding.stopPlaceImagebuttonNotFavorited.visibility = View.GONE
            } else {
                binding.stopPlaceImagebuttonFavorited.visibility = View.GONE
                binding.stopPlaceImagebuttonNotFavorited.visibility = View.VISIBLE
            }
        })
    }

}