package com.app.skyss_companion.view.planner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.skyss_companion.R
import com.app.skyss_companion.model.geocode.GeocodingFeature
import com.app.skyss_companion.model.travelplanner.TravelPlan
import com.app.skyss_companion.model.travelplanner.TravelStep
import com.app.skyss_companion.sampledata.TravelPlanListSample2
import com.app.skyss_companion.ui.theme.AppTheme
import com.app.skyss_companion.view.planner.data.FeatureType
import com.app.skyss_companion.view.planner.data.TravelPlannerTimeType
import com.app.skyss_companion.view.planner.location_search.LocationSearchComposeDialog
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun TravelPlannerScreenPreview() {
    val travelPlans = TravelPlanListSample2().travelPlanSample
    AppTheme {
        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
            PlannerInputContainer(
                {},
                {},
                ZonedDateTime.now(),
                { },
                TravelPlannerTimeType.DEPARTURE,
                {},
                null,
                null
            )
            PlannerResultsList(travelPlans) {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TravelPlannerScreen(
    onCardTapped: (t: TravelPlan) -> Unit,
    viewModel: TravelPlannerComposeViewModel = viewModel()
) {
    var fromFeaturesDialog by remember { mutableStateOf(false) }
    var toFeaturesDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val viewState by viewModel.viewState.collectAsState()
    val currentGeocodingFeatures by viewModel.geocodingFeatures.collectAsState()
    val isLoadingGeocodingFeatures by viewModel.isLoadingGeocode.collectAsState()
    val travelPlans by viewModel.travelPlans.collectAsState()

    AppTheme {
        if (fromFeaturesDialog) LocationSearchComposeDialog(
            onTextUpdated = { text -> viewModel.setDepartureSearchText(text) },
            onPropertySelected = {
                viewModel.setFeature(FeatureType.DEPARTURE, it)
                fromFeaturesDialog = false
                viewModel.clearDialogData()
            },
            onDialogDismissed = {
                fromFeaturesDialog = false
                viewModel.clearDialogData()
            },
            lastUsedGeocodingFeatures = null,
            currentGeocodingFeatures = currentGeocodingFeatures,
            isLoadingGeocodingFeatures = isLoadingGeocodingFeatures
        )

        if (toFeaturesDialog) LocationSearchComposeDialog(
            onTextUpdated = { text -> viewModel.setDestinationSearchText(text) },
            onPropertySelected = {
                viewModel.setFeature(FeatureType.DESTINATION, it)
                toFeaturesDialog = false
                viewModel.clearDialogData()
            },
            onDialogDismissed = {
                toFeaturesDialog = false
                viewModel.clearDialogData()
            },
            lastUsedGeocodingFeatures = null,
            currentGeocodingFeatures = currentGeocodingFeatures,
            isLoadingGeocodingFeatures = isLoadingGeocodingFeatures
        )

        if (showDatePicker)
            ComposeDatePickerDialog(
                onDismiss = {
                    showDatePicker = false
                },
                onConfirm = {
                    it?.let {
                        viewModel.setPlannerDate(it.year, it.monthValue, it.dayOfMonth)
                    }
                    showDatePicker = false
                    showTimePicker = true
                },
                currentDate = viewState.plannerTime
                //currentDate = viewModel.convertZonedDateTimeToLocalZone(viewState.plannerTime)
            )

        if (showTimePicker)
            ComposeTimePickerDialog(
                onDismiss = { showTimePicker = false },
                onConfirm = {
                    it?.let { time ->
                        viewModel.setPlannerTime(time.first, time.second)
                    }
                    showTimePicker = false
                },
                currentDate = viewState.plannerTime
            )

        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)) {
            PlannerInputContainer(
                onFromFeatureToggled = { fromFeaturesDialog = true },
                onToFeatureToggled = { toFeaturesDialog = true },
                currentDate = viewState.plannerTime,
                onShowDatePicker = { showDatePicker = true },
                timeType = viewState.timeType,
                onTimeTypeSelected = { t -> viewModel.setTimeType(t) },
                selectedDestinationFeature = viewState.selectedDestinationFeature,
                selectedDepartureFeature = viewState.selectedDepartureFeature
            )
            PlannerResultsList(travelPlans, onCardTapped)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PlannerInputContainer(
    onFromFeatureToggled: () -> Unit,
    onToFeatureToggled: () -> Unit,
    currentDate: ZonedDateTime,
    onShowDatePicker: () -> Unit,
    timeType: TravelPlannerTimeType,
    onTimeTypeSelected: (t: TravelPlannerTimeType) -> Unit,
    selectedDepartureFeature: GeocodingFeature?,
    selectedDestinationFeature: GeocodingFeature?,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            PlannerInputContainerButtonRow()
            PlannerInputContainerFromInput(
                onFromFeatureToggled = onFromFeatureToggled,
                selectedDepartureFeature = selectedDepartureFeature
            )
            PlannerInputContainerToInput(
                onToFeatureToggled = onToFeatureToggled,
                selectedDestinationFeature = selectedDestinationFeature
            )
            PlannerDateTimeSelectionRow(
                currentDate,
                onShowDatePicker,
                timeType,
                onTimeTypeSelected
            )
        }
    }
}

@Composable
fun PlannerInputContainerButtonRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}, content = @Composable {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_view_list_24),
                contentDescription = "Show Saved"
            )
        })
        TextButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_add_24),
                contentDescription = "Add"
            )
            Text("Lagre")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerInputContainerFromInput(
    onFromFeatureToggled: () -> Unit,
    selectedDepartureFeature: GeocodingFeature?
) {
    val featureLabel = selectedDepartureFeature?.properties?.label
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onFromFeatureToggled() },
            value = featureLabel ?: "Velg avreisested",
            onValueChange = { },
            label = { Text("Fra") },
            placeholder = { Text("Fra") },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(),
                disabledBorderColor = LocalContentColor.current.copy(),
                disabledLabelColor = LocalContentColor.current.copy(),
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerInputContainerToInput(
    onToFeatureToggled: () -> Unit,
    selectedDestinationFeature: GeocodingFeature?
) {
    val featureLabel = selectedDestinationFeature?.properties?.label
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToFeatureToggled() },
            value = featureLabel ?: "Velg destinasjon",
            onValueChange = { },
            label = { Text("Til") },
            placeholder = { Text("Til") },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(),
                disabledBorderColor = LocalContentColor.current.copy(),
                disabledLabelColor = LocalContentColor.current.copy(),
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun PlannerDateTimeSelectionRow(
    currentDate: ZonedDateTime,
    onShowDatePicker: () -> Unit,
    timeType: TravelPlannerTimeType,
    onTimeTypeSelected: (t: TravelPlannerTimeType) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Box {
                TextButton(onClick = { dropdownExpanded = true }) {
                    Text(if (timeType == TravelPlannerTimeType.DEPARTURE) "Avgang" else "Ankomst")
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_arrow_drop_down_24),
                        contentDescription = "Add"
                    )
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }) {
                    DropdownMenuItem(
                        text = @Composable { Text("Avgang") },
                        onClick = {
                            onTimeTypeSelected(TravelPlannerTimeType.DEPARTURE)
                            dropdownExpanded = false
                        }

                    )
                    DropdownMenuItem(
                        text = @Composable { Text("Ankomst") },
                        onClick = {
                            onTimeTypeSelected(TravelPlannerTimeType.ARRIVAL)
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }
        Column {
            TextButton(onClick = onShowDatePicker) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_calendar_today_24),
                    contentDescription = "Add"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(currentDate.format(formatter)) //TODO: Format date from current selected.
            }

        }
    }
}

@Composable
fun PlannerResultsList(travelPlans: List<TravelPlan>, onCardTapped: (t: TravelPlan) -> Unit) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        travelPlans.map { t -> PlannerResultListItem(t, onCardTapped) }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlannerResultListItem(travelPlan: TravelPlan, onCardTapped: (t: TravelPlan) -> Unit) {
    val start =
        "${
            travelPlan.startTime?.hour.toString().padEnd(2, '0')
        }:${travelPlan.startTime?.minute.toString().padEnd(2, '0')}"
    val end = "${travelPlan.endTime?.hour}:${travelPlan.endTime?.minute}"
    val startEpoch = travelPlan.startTime?.toEpochSecond() ?: 0
    val endEpoch = travelPlan.endTime?.toEpochSecond() ?: 0
    val duration = (endEpoch - startEpoch) / 60
    Card(modifier = Modifier
        .padding(bottom = 8.dp)
        .clickable { onCardTapped(travelPlan) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                Text("$start - $end", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("(${duration} minutter)")
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(verticalAlignment = Alignment.CenterVertically) {
                travelPlan.travelSteps.mapIndexed { i, t ->
                    TravelStepElement(t)
                    if (travelPlan.travelSteps.lastIndex != i) PlannerNextBox()
                }
            }
        }
    }
}

@Composable
fun TravelStepElement(travelStep: TravelStep) {
    return when (travelStep.type) {
        "route" -> {
            PlannerRouteBox(travelStep.routeDirection?.publicIdentifier ?: "?")
        }

        "walk" -> {
            PlannerWalkBox()
        }

        else -> {
            // TODO: Define TravelStepElement else block.
        }
    }
}

@Composable
fun PlannerWalkBox() {
    Box(
        modifier = Modifier
            .border(BorderStroke(2.dp, Color.Gray), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_directions_walk_24),
            contentDescription = "Next"
        )
    }
}

@Composable
fun PlannerRouteBox(lineNumber: String) {
    Box(
        modifier = Modifier
            .border(BorderStroke(2.dp, Color.Gray), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_directions_bus_24),
                contentDescription = "Next"
            )
            Text(lineNumber)
        }
    }
}

@Composable
fun PlannerNextBox() {
    Icon(
        painter = painterResource(R.drawable.ic_baseline_chevron_right_24),
        contentDescription = "Next"
    )
}



