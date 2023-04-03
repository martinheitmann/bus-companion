package com.app.skyss_companion.view.routedirection_timetable

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.skyss_companion.R
import com.app.skyss_companion.model.TimeTable
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.stop_place.StopGroupViewModel
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

val componentTag = "RDTimeTableScreen"

@Preview
@Composable
fun PreviewRouteDirectionTimeTableScreen() {
    val stopGroupName = "Holdeplass B"
    val routeDirectionName = "Til Holdeplass A"
    val lineNumber = "56"
    val isLoading = false
    val data: List<Pair<LocalDateTime, List<PassingTimeListItem>>> = listOf(
        Pair(
            LocalDateTime.of(2023, 7, 10, 10, 10),
            listOf(
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm11",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                ),
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm12",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                ),
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm13",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                )
            ),
        ),
        Pair(
            LocalDateTime.of(2023, 7, 11, 10, 10),
            listOf(
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm21",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                ),
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm22",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                ),
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm23",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                )
            ),
        ),
        Pair(
            LocalDateTime.of(2023, 7, 12, 10, 10),
            listOf(
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm31",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                ),
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm32",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                ),
                PassingTimeListItem(
                    timeStamp = LocalDateTime.now(),
                    displayTime = "dd:mm33",
                    isSelected = false,
                    tripIdentifier = "jf20vnb02"
                )
            ),
        )
    )
    AppTheme {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
            ButtonRow(onBackTapped = {}, onBookmarkTapped = {})
            RouteDirectionTableHeaderBox(routeDirectionName, lineNumber, stopGroupName)
            if (isLoading) LoadingIndicator() else DayTableColumnRow(data)
        }
    }
}

@Composable
fun RouteDirectionTimeTableScreen(
    viewModel: TimeTableComposeViewModel = viewModel(),
    stopGroupIdentifier: String,
    routeDirectionIdentifier: String,
    stopGroupName: String,
    routeDirectionName: String,
    lineCode: String,
) {
    val passingTimesData by viewModel.passingTimes.collectAsState(emptyList())
    val isLoading by viewModel.isLoadingTimetables.collectAsState(initial = false)
    viewModel.fetchTimeTables(stopGroupIdentifier, routeDirectionIdentifier)
    AppTheme {
        Column {
            ButtonRow(onBackTapped = {}, onBookmarkTapped = {})
            RouteDirectionTableHeaderBox(routeDirectionName, lineCode, stopGroupName)
            if (isLoading) LoadingIndicator() else DayTableColumnRow(passingTimesData)
        }
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
fun RouteDirectionTableHeaderBox(
    routeDirectionName: String,
    lineCode: String,
    stopGroupName: String,
) {
    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Text(stopGroupName, fontSize = 12.sp)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Color.Gray
                            )
                            .padding(8.dp)
                    )
                    {
                        Text(lineCode, color = Color.White)
                    }
                    Box(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                        Text(routeDirectionName)
                    }
                }
            }
        }
    }
}

@Composable
fun DayTableColumnRow(data: List<Pair<LocalDateTime, List<PassingTimeListItem>>>) {
    Log.d(componentTag, "row had ${data.size} items")
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
    ) {
        data.map { d -> DayTableColumn(data = d) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayTableColumn(data: Pair<LocalDateTime, List<PassingTimeListItem>>) {
    Log.d(componentTag, "column had ${data.second.size} items")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(end = 8.dp)
    ) {
        ColumnHeader(data.first)
        data.second.map { passingTime ->
            TimeDisplayBox(passingTime.displayTime)
        }
        /*stickyHeader {
            Text(
                data.first.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                textAlign = TextAlign.Center
            )
        }
        items(data.second.size) {index ->
            TimeDisplayBox(data.second[index].displayTime)
        }*/
    }
}

@Composable
fun ColumnHeader(data: LocalDateTime){
    Text(
        data.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        textAlign = TextAlign.Center
    )
}

@Composable
fun TimeDisplayBox(str: String) {
    Log.d(componentTag, "creating time display box with $str")
    Card(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
        Box(modifier = Modifier.padding(8.dp)) {
            Text(str)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        CircularProgressIndicator()
    }
}