package com.app.skyss_companion.view.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DepartureBoard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.skyss_companion.model.PassingTimeAlert
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.search.SearchStopsViewModel

@Preview
@Composable
fun PreviewActiveAlertsScreen() {
    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
        InfoBox()
    }
}

@Composable
fun ActiveAlertsScreen(activeAlertsViewModel: ActiveAlertsViewModel = viewModel()) {
    val alerts by activeAlertsViewModel.alerts.observeAsState()

    AppTheme {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
            InfoBox()
            alerts?.let { a ->
                if (a.isNotEmpty()) AlertsList(
                    alerts = a,
                    onAlertDeleteTapped = { activeAlertsViewModel.deletePassingTimeAlert(it) }) else EmptyListText()
            } ?: LoadingIndicator()
        }
    }
}

@Composable
fun LoadingIndicator() {
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun InfoBox() {
    Row {
        Card {
            Column(modifier = Modifier.padding(all = 8.dp)) {
                Box(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)) {
                    Text(text = "Varsler", fontSize = 18.sp)
                }
                Box(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Fjern og rediger varsler for valgte linjer fra bestemte holdeplasser.")
                }
            }
        }
    }
}

@Composable
fun EmptyListText() {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text("Ingen elementer å vise...", fontStyle = FontStyle.Italic)
    }
}

@Composable
fun AlertsList(alerts: List<PassingTimeAlert>, onAlertDeleteTapped: (PassingTimeAlert) -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        alerts.map { a -> AlertListElement(alert = a, onAlertDeleteTapped = onAlertDeleteTapped) }
    }
}

@Composable
fun AlertListElement(alert: PassingTimeAlert, onAlertDeleteTapped: (PassingTimeAlert) -> Unit) {
    val stopPlaceName = alert.stopName
    val lineNumber = alert.lineNumber
    val lineName = alert.directionName
    // Add leading 0's to the numbers for correct time formatting.
    val alertTime = "${String.format("%02d", alert.zonedAlertTimestamp.hour)}:${
        String.format(
            "%02d",
            alert.zonedAlertTimestamp.minute
        )
    }"
    val routeTime = "${String.format("%02d", alert.zonedRouteTimestamp.hour)}:${
        String.format(
            "%02d",
            alert.zonedRouteTimestamp.minute
        )
    }"
    Card(modifier = Modifier.padding(bottom = 8.dp)) {
        Column(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stopPlaceName)
                IconButton(
                    onClick = { onAlertDeleteTapped(alert) },
                    content = @Composable() {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Search",
                        )
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    Text(lineNumber, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                }
                Box {
                    Text(lineName, fontSize = 20.sp)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row {
                        Box(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                            Icon(Icons.Default.DepartureBoard, contentDescription = "Departure")
                        }
                        Box(modifier = Modifier.padding(all = 4.dp)) {
                            Text(routeTime)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(32.dp))
                Column {
                    Row {
                        Box(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alert Time")
                        }
                        Box(modifier = Modifier.padding(all = 4.dp)) {
                            Text(alertTime)
                        }
                    }
                }
            }
        }
    }
}