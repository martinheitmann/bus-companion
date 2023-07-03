package com.app.skyss_companion.view.bookmark

import androidx.compose.animation.Animatable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.skyss_companion.R
import com.app.skyss_companion.model.BookmarkedRouteDirection
import com.app.skyss_companion.model.StopGroup
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.bookmark.routedirections.BookmarkedRouteDirectionsListViewModel
import com.app.skyss_companion.view.bookmark.stopgroups.BookmarkedStopGroupsListViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
private fun previewSavedElementsView() {
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

    val routeDirections: List<BookmarkedRouteDirection> = listOf(
        BookmarkedRouteDirection(
            routeDirectionIdentifier = "id1",
            routeDirectionName = "Route Direction 1",
            lineCode = "11",
            stopGroupIdentifier = "id11",
            stopGroupName = "Stop Group 1"
        ),
        BookmarkedRouteDirection(
            routeDirectionIdentifier = "id2",
            routeDirectionName = "Route Direction 2",
            lineCode = "22",
            stopGroupIdentifier = "id22",
            stopGroupName = "Stop Group 2"
        ),
        BookmarkedRouteDirection(
            routeDirectionIdentifier = "id1",
            routeDirectionName = "Route Direction 1",
            lineCode = "11",
            stopGroupIdentifier = "id11",
            stopGroupName = "Stop Group 1"
        )
    )
    PreviewSavedElementsView(stopGroups, routeDirections)
}

@Composable
fun PreviewSavedElementsView(
    stopGroups: List<StopGroup>,
    routeDirections: List<BookmarkedRouteDirection>,
) {
    var currentIndex by remember { mutableStateOf(1) }
    AppTheme {
        Column {
            InfoBox()
            Spacer(modifier = Modifier.height(8.dp))
            TabView(currentIndex) { index -> currentIndex = index }
            if (currentIndex == 0) StopGroupList(
                stopGroups = stopGroups,
                onNavigateStopGroup = { }) else RouteDirectionList(
                routeDirections = routeDirections, onNavigateRouteDirection = { }
            )
        }
    }
}

@Composable
fun SavedElementsView(
    stopGroupViewModel: BookmarkedStopGroupsListViewModel = viewModel(),
    routeDirectionViewModel: BookmarkedRouteDirectionsListViewModel = viewModel(),
    onNavigateRouteDirection: (rd: BookmarkedRouteDirection) -> Unit,
    onNavigateStopGroup: (id: String) -> Unit,
) {
    val routeDirections by routeDirectionViewModel.bookmarkedRouteDirections.observeAsState()
    val stopGroups by stopGroupViewModel.bookmarkedStopGroups.observeAsState()
    var currentIndex by remember { mutableStateOf(0) }
    AppTheme {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
            InfoBox()
            Spacer(modifier = Modifier.height(8.dp))
            TabView(currentIndex) { index -> currentIndex = index }
            Crossfade(targetState = currentIndex) { index ->
                when(index) {
                    0 -> StopGroupList(
                        stopGroups = stopGroups,
                        onNavigateStopGroup = onNavigateStopGroup
                    )
                    1 -> RouteDirectionList(
                        routeDirections = routeDirections,
                        onNavigateRouteDirection = onNavigateRouteDirection
                    )
                }
            }
            /*if (currentIndex == 0) StopGroupList(
                stopGroups = stopGroups,
                onNavigateStopGroup = onNavigateStopGroup
            ) else RouteDirectionList(
                routeDirections = routeDirections,
                onNavigateRouteDirection = onNavigateRouteDirection
            )*/
        }
    }
}

@Composable
fun InfoBox() {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Lagrede holdeplasser og linjer",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Holdeplasser og linjer tilgjengelig i denne listen er også tilgjengelig som widget på hjem-skjermen.")
        }
    }
}

@Composable
fun TabView(currentIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabItem(
                isSelected = currentIndex == 0,
                text = "Holdeplasser",
                onTap = { onTabSelected(0) })
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabItem(
                isSelected = currentIndex == 1,
                text = "Linjer",
                onTap = { onTabSelected(1) })
        }
    }
}

@Composable
fun TabItem(isSelected: Boolean, text: String, onTap: () -> Unit) {

    Crossfade(targetState = isSelected) { selected ->
        when (selected) {
            true -> Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { },
                // border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50), // = 50% percent
                // or shape = CircleShape
            ) {
                Text(text = text)
            }
            false -> Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onTap() },
                //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50), // = 50% percent
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                // or shape = CircleShape
                // colors = ButtonDefaults.outlinedButtonColors(/*contentColor = MaterialTheme.colorScheme.primary*/)
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }

    /*if (isSelected)
        return Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { },
            // border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50), // = 50% percent
            // or shape = CircleShape
        ) {
            Text(text = text)
        } else
        return Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onTap() },
            //border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50), // = 50% percent
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            // or shape = CircleShape
            // colors = ButtonDefaults.outlinedButtonColors(/*contentColor = MaterialTheme.colorScheme.primary*/)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(0.7f)
            )
        }*/
}

@Composable
fun StopGroupList(
    stopGroups: List<StopGroup>?,
    onNavigateStopGroup: (id: String) -> Unit,
) {
    stopGroups?.let { s ->
        Column {
            s.map { sg ->
                StopGroupListItem(stopGroup = sg, onNavigateStopGroup)
            }
        }
    } ?: Text("Ingen gjenstander å vise....")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopGroupListItem(stopGroup: StopGroup, onNavigateStopGroup: (id: String) -> Unit) {
    val serviceModes = stopGroup.serviceModes ?: emptyList()
    val title = stopGroup.description ?: ""
    Card(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
        onClick = { onNavigateStopGroup(stopGroup.identifier) }
    ) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ServiceModeIcons(serviceModes = serviceModes)
                Spacer(modifier = Modifier.width(8.dp))
                StopGroupTitle(title = title)
            }
        }
    }
}

@Composable
fun RouteDirectionList(
    routeDirections: List<BookmarkedRouteDirection>?,
    onNavigateRouteDirection: (rd: BookmarkedRouteDirection) -> Unit,
) {
    routeDirections?.let { r ->
        Column {
            r.map { rd ->
                RouteDirectionListItem(routeDirection = rd, onNavigateRouteDirection)
            }
        }
    } ?: Text("Ingen gjenstander å vise....")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteDirectionListItem(
    routeDirection: BookmarkedRouteDirection,
    onNavigateRouteDirection: (rd: BookmarkedRouteDirection) -> Unit
) {
    val stopGroupName = routeDirection.stopGroupName
    val routeDirectionName = routeDirection.routeDirectionName
    val lineCode = routeDirection.lineCode
    Card(
        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
        onClick = { onNavigateRouteDirection(routeDirection) }
    ) {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                RouteDirectionStopGroupTitle(title = stopGroupName)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RouteDirectionLineCode(code = lineCode)
                    Spacer(modifier = Modifier.width(8.dp))
                    RouteDirectionTitle(title = routeDirectionName)
                }
            }
        }
    }
}

@Composable
fun ServiceModeIcons(serviceModes: List<String>) {
    Row {
        if (serviceModes.contains("Bus"))
            BusIcon()
        if (serviceModes.contains("Light rail"))
            RailIcon()
    }
}

@Composable
fun RouteDirectionStopGroupTitle(title: String) {
    Text(title, fontSize = 12.sp)
}

@Composable
fun RouteDirectionTitle(title: String) {
    Text(title, fontSize = 18.sp)
}

@Composable
fun RouteDirectionLineCode(code: String) {
    Text(code, fontWeight = FontWeight.Bold, fontSize = 22.sp)
}

@Composable
fun StopGroupTitle(title: String) {
    Text(title)
}

@Composable
fun BusIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_directions_bus_24),
        contentDescription = "Bus"
    )
}

@Composable
fun RailIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_directions_railway_24),
        contentDescription = "Rail"
    )
}
