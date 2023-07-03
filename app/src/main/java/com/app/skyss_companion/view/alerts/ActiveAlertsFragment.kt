package com.app.skyss_companion.view.alerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skyss_companion.databinding.ActiveAlertsFragmentBinding
import com.app.skyss_companion.view.search.SearchStopsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveAlertsFragment : Fragment() {

    val TAG = "ActiveAlertsFragment"
    private val viewModel: ActiveAlertsViewModel by viewModels()

    /*lateinit var adapter: ActiveAlertsListAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    private var _binding: ActiveAlertsFragmentBinding? = null
    private val binding get() = _binding!!*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //_binding = ActiveAlertsFragmentBinding.inflate(inflater, container, false)
        //return binding.root

        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ActiveAlertsScreen()
            }
        }
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView = binding.activeAlertsRecyclerview
        layoutManager = LinearLayoutManager(requireContext())
        adapter = ActiveAlertsListAdapter { viewModel.deletePassingTimeAlert(it) }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        viewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            adapter.setData(alerts)
            binding.activeAlertsEmptyText.visibility = if(alerts.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }*/

}