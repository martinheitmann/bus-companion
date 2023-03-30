package com.app.skyss_companion.view.stop_place

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StopGroupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val identifier = arguments?.getString("STOP_IDENTIFIER")
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StopGroupScreen(
                    identifier = identifier ?: "",
                    onBackTapped = ::navigateBack,
                    onBookmarkTapped = ::toggleBookmark
                )
            }
        }
    }

    private fun navigateBack(){
        findNavController().popBackStack()
    }

    private fun toggleBookmark(){

    }
}