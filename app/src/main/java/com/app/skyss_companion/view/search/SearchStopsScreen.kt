package com.app.skyss_companion.view.search

import android.app.SearchableInfo
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.skyss_companion.model.RecentlyUsed
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.bookmark.ServiceModeIcons
import com.app.skyss_companion.view.bookmark.StopGroupTitle
import com.app.skyss_companion.view.widgets.StopGroupListItem

@Preview
@Composable
fun PreviewSearchStopsScreen() {
    val isLoading = true
    val stopGroups: List<StopGroup> = listOf(
        StopGroup(
            description = "Stop Group 1",
            serviceModes = listOf("Bus"),
            identifier = "id1",
        ),
        StopGroup(
            description = "Stop Group 2",
            serviceModes = listOf("Bus"),
            identifier = "id2",
        ),
        StopGroup(
            description = "Stop Group 3",
            serviceModes = listOf("Bus"),
            identifier = "id3",
        )
    )
    AppTheme() {
        Column {
            SearchBox(text = "", onSearchTextChange = {})
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading) LoadingIndicator() else SearchResultsList(
                stopGroups = stopGroups,
                onListElementTapped = {})
        }
    }
}

@Composable
fun SearchStopsScreen(
    searchStopsViewModel: SearchStopsViewModel = viewModel(),
    onStopGroupSelected: (String) -> Unit
) {
    val recentlyUsedStopGroups by searchStopsViewModel.recentlyUsedStopGroups.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val stopSearchResults by searchStopsViewModel.stopSearchResults.observeAsState()
    val isLoading by searchStopsViewModel.isLoading.observeAsState()
    val isSyncing by searchStopsViewModel.isSyncing.observeAsState()

    AppTheme() {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
            SearchBox(text = "", onSearchTextChange = { searchStopsViewModel.filterResults((it)) })
            Spacer(modifier = Modifier.height(8.dp))
            if (isLoading == true) LoadingIndicator() else SearchResultsList(
                stopGroups = stopSearchResults ?: emptyList(),
                onListElementTapped = { stopGroup -> onStopGroupSelected(stopGroup.identifier) })
        }
    }
}

@Composable
fun SearchBox(text: String?, onSearchTextChange: (String) -> Unit) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 32.dp)
        ) {
            SearchTitle()
            SearchTextField(onSearchTextChange = { onSearchTextChange(it) }, text = text)
        }
    }
}

@Composable
fun SearchResultsList(stopGroups: List<StopGroup>, onListElementTapped: (StopGroup) -> Unit) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        if (stopGroups.isNotEmpty()) Text("Viser ${stopGroups.size} søkeresultater")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(stopGroups) { stopGroup ->
                StopGroupListItem(stopGroup = stopGroup, onTap = { onListElementTapped(stopGroup) })
            }
        }
    }
}

@Composable
fun SearchTitle() {
    Row {
        Text("Søk holdeplasser")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(text: String?, onSearchTextChange: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    Row {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30),
            value = searchText,
            onValueChange = { text ->
                searchText = text
                if (text.length > 2) {
                    Log.d("SearchTextField", "onValueChange $text")
                    onSearchTextChange(text)
                }
            },
            label = { Text("Søk etter holdeplasser") },
            trailingIcon = @Composable {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                )
            }
        )
    }
}

@Composable
fun LoadingIndicator() {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().padding(top = 32.dp)) {
        CircularProgressIndicator()
    }
}