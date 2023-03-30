package com.app.skyss_companion.view.stop_place

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.stop_place.model.StopGroupDeparturesEntry
import com.app.skyss_companion.view.stop_place.model.StopGroupListItem
import com.app.skyss_companion.view.stop_place.model.StopGroupNameDivider
import com.app.skyss_companion.R

@Preview
@Composable
fun PreviewStopGroupScreen() {
    var name = "Holdeplass 1"
    var data = mutableListOf(
        StopGroupNameDivider("Loddefjord Terminal A"),
        StopGroupDeparturesEntry(
            identifier = "id1",
            lineCode = "3",
            description = "Til Åsane Terminal",
            departures = listOf("3 min", "17:30", "18:30", "19:00", "19:15")
        ),
        StopGroupDeparturesEntry(
            identifier = "id2",
            lineCode = "20",
            description = "Til Haukeland Sykehus",
            departures = listOf("11 min", "17:43", "18:15", "18:30")
        ),
        StopGroupNameDivider("Loddefjord Terminal B"),
        StopGroupDeparturesEntry(
            identifier = "id1",
            lineCode = "3",
            description = "Til Vadmyra",
            departures = listOf("3 min", "17:30", "18:30", "19:00", "19:15")
        ),
        StopGroupDeparturesEntry(
            identifier = "id2",
            lineCode = "20",
            description = "Til Storavatnet Terminal",
            departures = listOf("11 min", "17:43", "18:15", "18:30")
        )
    )
    data = mutableListOf<StopGroupListItem>()
    AppTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            ButtonRow({}, {})
            StopGroupHeaderBox(
                stopGroupName = name,
                availableLinecodes = data.filterIsInstance<StopGroupDeparturesEntry>()
                    .map { d -> d.lineCode },
                selectedLinecodes = emptyList()
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (data.isNotEmpty()) StopGroupList(data) else EmptyListView()
        }
    }
}

@Composable
fun StopGroupScreen(
    identifier: String,
    stopGroupViewModel: StopGroupViewModel = viewModel(),
    onBackTapped: () -> Unit,
    onBookmarkTapped: () -> Unit
) {
    val data by stopGroupViewModel.stopGroup.collectAsState()
    val dataList by stopGroupViewModel.stopGroupList.collectAsState(initial = emptyList())
    val isLoading by stopGroupViewModel.isLoadingStopGroup.collectAsState()
    stopGroupViewModel.updateStopGroup(identifier)
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            ButtonRow(onBackTapped, onBookmarkTapped)
            StopGroupHeaderBox(
                stopGroupName = data?.description ?: "Ukjent holdeplass",
                availableLinecodes = data?.lineCodes ?: emptyList<String>(),
                selectedLinecodes = emptyList()
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (dataList.isNotEmpty()) StopGroupList(dataList) else EmptyListView()
        }
    }
}

@Composable
fun EmptyListView() {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Text("Ingen elementer å vise...", fontStyle = FontStyle.Italic)
        }
    }
}

@Composable
fun StopGroupHeaderBox(
    stopGroupName: String,
    availableLinecodes: List<String>,
    selectedLinecodes: List<String>,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column {
                Row {
                    Text(stopGroupName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                StopGroupLineCodes(availableLinecodes, selectedLinecodes, isExpanded)
                if (availableLinecodes.size > 6)
                    ExpandShrinkButton(isExpanded) { isExpanded = !isExpanded }
            }
        }
    }
}

@Composable
fun ExpandShrinkButton(isExpanded: Boolean, onToggled: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = onToggled, content = @Composable() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isExpanded)
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_add_24),
                        contentDescription = "Show more"
                    )
                else Icon(
                    painter = painterResource(R.drawable.baseline_remove_24),
                    contentDescription = "Show less"
                )
                Text(if (isExpanded) "Vis mindre" else "Vis mer")
            }
        })
    }
}

@Composable
fun ButtonRow(onBackTapped: () -> Unit, onBookmarkTapped: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = onBackTapped, content = @Composable {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_arrow_back_24),
                contentDescription = "Back"
            )
        })
        IconButton(onClick = onBookmarkTapped, content = @Composable() {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_bookmark_24),
                contentDescription = "Bookmark"
            )
        })
    }
}

@Composable
fun StopGroupLineCodesAvailable(available: List<String>, isExpanded: Boolean) {
    var availableSize = available.size
    if (!isExpanded && availableSize > 6) availableSize = 6
    Row {
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
        ) {
            items(availableSize) { index ->
                val item = available[index]
                LineCodeItem(t = item)
            }
        }
    }
}

@Composable
fun LineCodeItem(t: String) {
    Box(
        modifier = Modifier
            .padding(all = 4.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    )
    {
        Box(modifier = Modifier.padding(8.dp)) {
            Text(text = t, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun StopGroupLineCodes(available: List<String>, selected: List<String>, isExpanded: Boolean) {
    Row {
        Column {
            StopGroupLineCodesAvailable(available, isExpanded)
        }
    }
}

@Composable
fun StopGroupList(items: List<StopGroupListItem>) {
    Row {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            items.forEach { item ->
                when (item) {
                    is StopGroupDeparturesEntry -> StopGroupDepartures(item)
                    is StopGroupNameDivider -> StopGroupListDivider(item)
                }
            }
        }
    }
}

@Composable
fun StopGroupDepartures(entry: StopGroupDeparturesEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(entry.lineCode, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(entry.description)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (entry.departures.isNotEmpty()) entry.departures
                    .take(5)
                    .map { d ->
                        Box(modifier = Modifier.padding(end = 12.dp)) {
                            Text(d)
                        }
                    } else Text("Ingen flere avganger i dag...")
            }
        }
    }
}

@Composable
fun StopGroupListDivider(item: StopGroupNameDivider) {
    Row(modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)) {
        Text(item.name)
    }
}