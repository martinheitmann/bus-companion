package com.app.skyss_companion.view.planner


import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

/*
@ExperimentalMaterial3Api
@Composable
fun ComposeDatePickerDialog() {
    /*Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = ZonedDateTime.now().toEpochSecond(),
            yearRange = IntRange(LocalDateTime.now().year - 1, LocalDateTime.now().year + 1),
            initialDisplayMode = DisplayMode.Picker
        )
        DatePicker(state = datePickerState)
    }*/
}
*/

@ExperimentalMaterial3Api
@Composable
fun ComposeDatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (z: ZonedDateTime?) -> Unit,
    currentDate: ZonedDateTime?
) {
    val datePickerState = rememberDatePickerState(
        //initialSelectedDateMillis = currentDate.toEpochSecond(),
        //yearRange = IntRange(currentDate.year - 1, currentDate.year + 1),
        initialDisplayMode = DisplayMode.Picker
    )
    val formatter = DatePickerFormatter(

    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = currentDate != null,
                onClick = {
                    datePickerState.selectedDateMillis?.let { ms ->
                        val instant = Instant.ofEpochMilli(ms)
                        val zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
                        onConfirm(zdt)
                    }
                }) {
                Text("Bekreft")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Avbryt")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}