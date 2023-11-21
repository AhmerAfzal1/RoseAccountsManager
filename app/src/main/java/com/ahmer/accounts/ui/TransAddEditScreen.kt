package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.dialogs.DateTimePickerDialog
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.TransAddEditState
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.CurrencyIcon
import com.ahmer.accounts.utils.DateIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MyTextField
import com.ahmer.accounts.utils.NotesIcon
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransAddEditScreen(
    viewModel: TransAddEditViewModel,
    viewModelSettings: SettingsViewModel,
    onPopBackStack: () -> Unit
) {
    val mContext: Context = LocalContext.current
    val mCurrency: Currency by viewModelSettings.currentCurrency.collectAsStateWithLifecycle()
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mLenDes = 64
    val mState: TransAddEditState by viewModel.uiState.collectAsStateWithLifecycle()
    val mTransEntity: TransEntity = mState.transaction ?: TransEntity()
    val isEditMode: Boolean = viewModel.isEditMode
    var mDatePickerDialog: Boolean by rememberSaveable { mutableStateOf(value = false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                else -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    if (mDatePickerDialog) {
        DateTimePickerDialog(selectedDate = mTransEntity.date) {
            viewModel.onEvent(TransAddEditEvent.OnDateChange(it))
        }
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Scaffold(modifier = Modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = Constants.TOP_APP_BAR_HEIGHT)
                    .bottomBorder(strokeWidth = 2.dp, color = Color.LightGray.copy(alpha = 0.2f)),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        clear()
                        onPopBackStack()
                    }, modifier = Modifier.size(size = Constants.ICON_SIZE)
                ) { BackIcon() }
                Text(
                    text = if (!isEditMode) "Add Transaction" else "Edit Transaction",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val mOptions: List<String> = listOf(Constants.TYPE_CREDIT, Constants.TYPE_DEBIT)
                var mSelectedIndex: Int by remember { mutableIntStateOf(value = 2) }

                MyTextField(
                    value = HelperUtils.getDateTime(
                        time = mTransEntity.date, pattern = Constants.DATE_TIME_NEW_PATTERN
                    ),
                    onValueChange = {},
                    modifier = Modifier.onFocusChanged { mDatePickerDialog = it.isFocused },
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.label_date)) },
                    leadingIcon = { DateIcon() },
                    trailingIcon = {},
                    supportingText = {})

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    mOptions.forEachIndexed { index, label ->
                        val mType: AnnotatedString = buildAnnotatedString {
                            append(text = label)
                            if (label == Constants.TYPE_CREDIT) append(" (+)") else append(" (-)")
                        }
                        SegmentedButton(
                            selected = mTransEntity.type == label,
                            onClick = {
                                mSelectedIndex = index
                                viewModel.onEvent(TransAddEditEvent.OnTypeChange(label))
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index, count = mOptions.size
                            ),
                        ) {
                            Text(text = mType.text.uppercase())
                        }
                    }
                }

                MyTextField(
                    value = mTransEntity.amount,
                    onValueChange = {
                        viewModel.onEvent(TransAddEditEvent.OnAmountChange(it.trim()))
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester = mFocusRequester)
                        .onFocusChanged { focus ->
                            if (focus.isFocused) {
                                mKeyboardController?.show()
                            }
                        },
                    label = { Text(stringResource(id = R.string.label_by_amount)) },
                    leadingIcon = { CurrencyIcon() },
                    trailingIcon = {
                        if (mTransEntity.amount.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (mTransEntity.amount.isNotEmpty()) {
                                    viewModel.onEvent(TransAddEditEvent.OnAmountChange(""))
                                }
                            })
                        }
                    },
                    prefix = { Text(text = mCurrency.symbol) },
                    supportingText = { },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        mFocusManager.moveFocus(FocusDirection.Down)
                    })
                )

                MyTextField(
                    value = mTransEntity.description,
                    onValueChange = {
                        if (it.length <= mLenDes) {
                            viewModel.onEvent(TransAddEditEvent.OnDescriptionChange(it.trim()))
                        }
                    },
                    label = { Text(stringResource(id = R.string.label_description)) },
                    leadingIcon = { NotesIcon() },
                    trailingIcon = {
                        if (mTransEntity.description.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (mTransEntity.description.isNotEmpty()) {
                                    viewModel.onEvent(
                                        TransAddEditEvent.OnDescriptionChange(
                                            description = ""
                                        )
                                    )
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${mTransEntity.description.length} / $mLenDes",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { clear() }),
                    maxLines = 3
                )

                OutlinedButton(
                    onClick = {
                        clear()
                        viewModel.onEvent(TransAddEditEvent.OnSaveClick)
                    },
                    enabled = mTransEntity.amount.isNotEmpty()
                ) {
                    Text(text = if (!isEditMode) "Save".uppercase() else "Update".uppercase())
                }
            }
        }
    }
}