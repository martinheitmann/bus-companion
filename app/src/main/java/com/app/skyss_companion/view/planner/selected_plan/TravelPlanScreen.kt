package com.app.skyss_companion.view.planner.selected_plan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.TableInfo
import com.app.skyss_companion.R
import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.travelplanner.Intermediate
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelStep
import com.app.skyss_companion.sampledata.TravelPlanSample
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.stop_place.StopGroupViewModel
import com.google.android.material.button.MaterialButton
import kotlin.math.exp
import kotlin.time.Duration

@Preview
@Composable
fun TravelPlanScreenPreview() {
    val travelPlan: TravelPlan? = TravelPlanSample().travelPlanSample
    AppTheme { TravelPlanContainer(travelPlan, {}, {}) }
}

@Composable
fun TravelPlanScreen(
    identifier: String,
    onBackTapped: () -> Unit,
    onBookmarkTapped: () -> Unit,
    viewModel: SelectedTravelPlanViewModel = viewModel(),
) {
    val travelPlan by viewModel.selectedTravelPlan.observeAsState()
    viewModel.fetchTravelPlan(identifier)
    AppTheme { TravelPlanContainer(travelPlan, onBackTapped, onBookmarkTapped) }
}


@Composable
fun TravelPlanContainer(
    travelPlan: TravelPlan?, onBackTapped: () -> Unit,
    onBookmarkTapped: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        ButtonRow(onBackTapped, onBookmarkTapped)
        travelPlan?.let { t -> TravelPlanBody(t) } ?: LoadingIndicator()
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
fun TravelPlanDurationHeader(travelPlan: TravelPlan) {
    val startHour = travelPlan.startTime?.hour
    val startMin = travelPlan.startTime?.minute
    val endHour = travelPlan.endTime?.hour
    val endMin = travelPlan.endTime?.minute

    val startEpoch = travelPlan.startTime?.toEpochSecond() ?: 0
    val endEpoch = travelPlan.endTime?.toEpochSecond() ?: 0
    val duration = (endEpoch - startEpoch) / 60
    Row {
        Text("$startHour:$startMin - $endHour:$endMin", fontSize = 12.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Text("($duration minutter)", fontSize = 12.sp)
    }
}

@Composable
fun TravelPlanBody(travelPlan: TravelPlan?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        travelPlan?.let { t ->
            Column(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
            ) {
                TravelPlanDurationHeader(travelPlan)
                Spacer(modifier = Modifier.height(16.dp))
                travelPlan.travelSteps.map { t -> TravelStepElement(t) }
                TravelPlanEnd(travelPlan)
            }
        } ?: LoadingIndicator()
    }
}

@Composable
fun TravelPlanEnd(travelPlan: TravelPlan) {
    val endHour = travelPlan.endTime?.hour
    val endMin = travelPlan.endTime?.minute
    Row {
        Text("$endHour:$endMin")
        Spacer(modifier = Modifier.width(4.dp))
        Text(travelPlan.end?.description ?: "N/A")
    }
}

@Composable
fun TravelStepElement(travelStep: TravelStep) {
    when (travelStep.type) {
        "walk" -> TravelStepWalk(travelStep)
        "route" -> TravelStepRoute(travelStep, travelStep.intermediates)
    }
}

@Composable
fun TravelStepWalk(travelStep: TravelStep) {
    val startMinute = travelStep.startTime?.minute
    val startHour = travelStep.startTime?.hour
    val startLocation = travelStep.stop?.description ?: "N/A"
    val walkDuration = travelStep.duration?.let { getDurationMinutes(Duration.parse(it)) } ?: "N/A"
    val walkDistance = travelStep.distance ?: "N/A"
    Box {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Column {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .background(Color.Gray)
                        .width(2.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Row {
                    Text("$startHour:$startMinute", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(startLocation, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.padding(top = 32.dp, bottom = 48.dp)) {
                    WalkIcon()
                    Text(walkDuration)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("($walkDistance meter)")
                }
            }
        }

    }
}

@Composable
fun TravelStepRoute(travelStep: TravelStep, intermediates: List<Intermediate>) {
    val startMinute = travelStep.startTime?.minute
    val startHour = travelStep.startTime?.hour
    val stopName = travelStep.stop?.description ?: "N/A"
    val routeDirectionName = travelStep.routeDirection?.directionName ?: "N/A"

    var expanded: Boolean by remember { mutableStateOf(false) }

    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column {
            Box(
                Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
                    .width(2.dp)
            )
        }
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Row {
                Text("$startHour:$startMinute", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(stopName, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                RouteDirectionIcon(travelStep.routeDirection)
                Text(routeDirectionName)
            }
            Row {
                if (travelStep.intermediates.isNotEmpty()) RouteIntermediates(
                    intermediates,
                    expanded
                ) { value ->
                    expanded = value
                } else Spacer(modifier = Modifier.height(32.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RouteDirectionIcon(routeDirection: RouteDirection?) {
    return when (routeDirection?.serviceMode) {
        "Bus" -> Icon(
            painter = painterResource(R.drawable.ic_baseline_directions_bus_24),
            contentDescription = "Add"
        )

        else ->
            Icon(
                painter = painterResource(R.drawable.ic_baseline_help_outline_24),
                contentDescription = "Add"
            )
    }
}

@Composable
fun WalkIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_directions_walk_24),
        contentDescription = "Add"
    )
}

@Composable
fun ExpandIcon(onTapped: (value: Boolean) -> Unit) {
    IconButton(onClick = { onTapped(true) }) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_arrow_drop_down_24),
            contentDescription = "Add"
        )
    }

}

@Composable
fun CollapseIcon(onTapped: (value: Boolean) -> Unit) {
    IconButton(onClick = { onTapped(false) }) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_arrow_drop_up_24),
            contentDescription = "Add"
        )
    }
}

@Composable
fun RouteIntermediates(
    intermediates: List<Intermediate>,
    expanded: Boolean,
    onToggle: (value: Boolean) -> Unit
) {
    val fontSize = 12.sp
    Column(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 32.dp)) {
        Row {
            TextButton(modifier = Modifier.padding(0.dp), onClick = {}) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${intermediates.size} stopp")
                    if (expanded) CollapseIcon { value -> onToggle(value) }
                    else ExpandIcon { value -> onToggle(value) }
                }
            }
        }
        if (expanded)
            intermediates.map { intermediate ->
                Row(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)) {
                    Text(
                        intermediate.aimedTime ?: "N/A",
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(intermediate.stopName ?: "N/A", fontSize = fontSize)
                }
            }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

fun getDurationMinutes(d: Duration): String {
    return if (d.inWholeMinutes.toInt() == 1) "1 minutt"
    else "${d.inWholeMinutes.toInt()} minutter"
}
