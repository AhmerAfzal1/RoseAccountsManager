package com.ahmer.accounts.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R
import java.util.Calendar

internal fun getDateTimeInMillis(dateMillis: Long, hours: Int, minutes: Int): Long {
    val mCalender = Calendar.getInstance().apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, minutes)
    }
    return mCalender.timeInMillis
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(selectedDate: Long, onDateTimeSelected: (Long) -> Unit) {
    var mDatePickerDialog: Boolean by remember { mutableStateOf(value = true) }
    var mTimePickerDialog: Boolean by remember { mutableStateOf(value = false) }
    val mBackgroundColor: Color = MaterialTheme.colorScheme.background
    val mDatePickerState: DatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate, initialDisplayMode = DisplayMode.Picker
    )
    val mTimePickerState: TimePickerState = rememberTimePickerState(is24Hour = false)

    if (mDatePickerDialog) {
        val mConfirmEnabled: State<Boolean> = remember {
            derivedStateOf { mDatePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = { mDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        mDatePickerDialog = false
                        mTimePickerDialog = true
                    }, enabled = mConfirmEnabled.value
                ) { Text(text = stringResource(id = R.string.label_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { mDatePickerDialog = false }) {
                    Text(text = stringResource(id = R.string.label_cancel))
                }
            },
            colors = DatePickerDefaults.colors().copy(containerColor = mBackgroundColor)
        )
        {
            DatePicker(
                state = mDatePickerState,
                colors = DatePickerDefaults.colors().copy(containerColor = mBackgroundColor)
            )
        }
    }

    //Time picker dialog
    if (mTimePickerDialog) {
        AlertDialog(
            onDismissRequest = { mTimePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    mTimePickerDialog = false
                    mDatePickerState.selectedDateMillis?.let { dateMilli ->
                        onDateTimeSelected(
                            getDateTimeInMillis(
                                dateMillis = dateMilli,
                                hours = mTimePickerState.hour,
                                minutes = mTimePickerState.minute
                            )
                        )
                    }
                }) { Text(text = stringResource(id = R.string.label_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { mTimePickerDialog = false }) {
                    Text(text = stringResource(id = R.string.label_cancel))
                }
            },
            text = {
                TimePicker(
                    state = mTimePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                        selectorColor = MaterialTheme.colorScheme.primary,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    layoutType = TimePickerLayoutType.Vertical
                )
            },
            containerColor = mBackgroundColor,
        )
    }
}