package com.app.skyss_companion.view.planner


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.ZonedDateTime


@ExperimentalMaterial3Api
@Composable
fun ComposeTimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (time: Pair<Int, Int>?) -> Unit,
    currentDate: ZonedDateTime?
) {
    val now = ZonedDateTime.now()
    val timePickerState = rememberTimePickerState(
        initialHour = currentDate?.hour ?: now.hour,
        initialMinute = currentDate?.minute ?: now.minute,
        is24Hour = true
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    TimePicker(
                        state = timePickerState,
                    )
                }
                Row(horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Avbryt")
                    }
                    TextButton(
                        onClick = {
                            onConfirm(Pair(timePickerState.hour, timePickerState.minute))
                        }
                    ) {
                        Text("Bekreft")
                    }
                }
            }
        }
    }
}