package com.app.skyss_companion.view.stop_place

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.StopPlaceFragmentBinding
import com.app.skyss_companion.misc.StopPlaceUtils
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint

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

        adapter = StopPlaceAdapter(requireContext())
        lineCodesAdapter = StopPlaceLineCodeAdapter(requireContext(), onTap = {index -> viewModel.addLineCodeToFilter(index)})
        lineCodesFilterAdapter = StopPlaceFilterLineCodeAdapter(requireContext(), onTap = {index -> viewModel.removeLineCodeFromFilter(index)})
        recyclerView = binding.stopPlaceRecyclerview
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        lineCodesRecyclerView = binding.stopPlaceLinecodesRecyclerview
        lineCodesFilterRecyclerView = binding.stopPlaceRecyclerviewLinecodeFilter
        val identifier = arguments?.getString("STOP_IDENTIFIER")

        /*flexboxLayoutManager = FlexboxLayoutManager(context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.CENTER
        }*/

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
            binding.stopPlaceImagebuttonFavorited.setOnClickListener { viewModel.removeFavorited(identifier) }
            binding.stopPlaceImagebuttonNotFavorited.setOnClickListener { viewModel.addFavorited(identifier) }
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

        viewModel.isFavorited.observe(viewLifecycleOwner, { value ->
            if(value){
                binding.stopPlaceImagebuttonFavorited.visibility = View.VISIBLE
                binding.stopPlaceImagebuttonNotFavorited.visibility = View.GONE
            } else {
                binding.stopPlaceImagebuttonFavorited.visibility = View.GONE
                binding.stopPlaceImagebuttonNotFavorited.visibility = View.VISIBLE
            }
        })

        binding.stopPlaceBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}