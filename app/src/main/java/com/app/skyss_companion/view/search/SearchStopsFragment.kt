package com.app.skyss_companion.view.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.R
import com.app.skyss_companion.databinding.SearchStopsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStopsFragment : Fragment() {
    val TAG = "SearchStopsFragment"
    private val viewModel: SearchStopsViewModel by viewModels()
    private lateinit var adapter: SearchViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private var _binding: SearchStopsFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchStopsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = SearchViewAdapter(onItemTapped = {s: String -> navigateToStopPlace(s)} )
        recyclerView = binding.searchStopsRecyclerview
        layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        binding.searchStopsTextviewItemCount.visibility = View.GONE
        viewModel.stopSearchResults.observe(viewLifecycleOwner, {
            Log.d(TAG, "Observer triggered with ${it.size} items")
            adapter.setData(it)
            if(it.isEmpty()){
                binding.searchStopsTextviewItemCount.text = "Ingen søkeresultater å vise"
                binding.searchStopsTextviewItemCount.visibility = View.VISIBLE
            }
            else {
                if(it.size == 1){
                    binding.searchStopsTextviewItemCount.text = "Viser 1 søkeresultat"
                }
                else {
                    binding.searchStopsTextviewItemCount.text = "Viser ${it.size} søkeresultater"
                }
                binding.searchStopsTextviewItemCount.visibility = View.VISIBLE
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            toggleLoadingWidget(it)
        })

        viewModel.isSyncing.observe(viewLifecycleOwner, {
            if(it) binding.searchStopsCardviewLoading.visibility = View.VISIBLE
            else binding.searchStopsCardviewLoading.visibility = View.GONE
        })

        binding.searchStopsEdittext.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Don't need this method
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null && s.length >= 3){
                    val sb: StringBuilder = StringBuilder(s.length)
                    sb.append(s)
                    viewModel.filterResults(sb.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Don't need this method
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.cancelJob()
    }

    private fun toggleLoadingWidget(value: Boolean){
        if(value){
            binding.searchStopsLoadingProgressBar.visibility = View.VISIBLE
        } else {
            binding.searchStopsLoadingProgressBar.visibility = View.GONE
        }
    }

    private fun navigateToStopPlace(stopIdentifier: String){
        val bundle = Bundle()
        bundle.putString("STOP_IDENTIFIER", stopIdentifier)
        findNavController().navigate(R.id.action_tabsContainerFragment_to_stopPlaceFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}