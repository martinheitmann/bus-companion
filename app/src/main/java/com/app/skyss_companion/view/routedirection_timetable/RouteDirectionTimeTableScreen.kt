package com.app.skyss_companion.view.routedirection_timetable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.skyss_companion.model.TimeTable
import com.app.skyss_companion.ui.theme.AppTheme
import java.time.LocalDateTime

@Preview
@Composable
fun PreviewRouteDirectionTimeTableScreen() {
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
        Column {
            RouteDirectionTableHeaderBox()
            DayTableColumnRow(data)
        }
    }
}

@Composable
fun RouteDirectionTableHeaderBox() {
    Row {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text("Hello")
        }
    }
}


@Composable
fun DayTableColumnRow(data: List<Pair<LocalDateTime, List<PassingTimeListItem>>>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        DayTableColumn(data[0])
        DayTableColumn(data[1])
        DayTableColumn(data[2])
    }
}

@Composable
fun DayTableColumn(data: Pair<LocalDateTime, List<PassingTimeListItem>>) {
    Column(modifier = Modifier.fillMaxHeight().padding(end = 8.dp)) {
        Text(data.first.dayOfWeek.toString())
        data.second.map { passingTime ->
            Text(passingTime.displayTime)
        }
    }
}