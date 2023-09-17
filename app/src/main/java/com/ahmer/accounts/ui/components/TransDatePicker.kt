package com.ahmer.accounts.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransDatePickDialog(onEvent: (TransAddEditEvent) -> Unit) {
    val mOpenDialog = remember { mutableStateOf(true) }

    if (mOpenDialog.value) {
        val mDatePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { mDatePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(onDismissRequest = { mOpenDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        mOpenDialog.value = false
                        mDatePickerState.selectedDateMillis?.let {
                            val mDate = HelperUtils.getDateTime(it, Constants.DATE_PATTERN)
                            onEvent(TransAddEditEvent.OnDateChange(mDate))
                        }
                    }, enabled = confirmEnabled.value
                ) { Text(text = stringResource(id = R.string.label_ok)) }
            }, dismissButton = {
                TextButton(onClick = { mOpenDialog.value = false }) {
                    Text(text = stringResource(id = R.string.label_cancel))
                }
            }) {
            DatePicker(state = mDatePickerState)
        }
    }
}