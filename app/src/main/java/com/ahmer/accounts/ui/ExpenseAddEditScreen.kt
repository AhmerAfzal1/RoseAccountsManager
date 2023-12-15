package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.model.CategoryModel
import com.ahmer.accounts.dialogs.DateTimePickerDialog
import com.ahmer.accounts.event.ExpenseAddEditEvent
import com.ahmer.accounts.event.UiEvent
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddEditScreen(
    viewModel: ExpenseAddEditViewModel,
    viewModelSettings: SettingsViewModel,
    onPopBackStack: () -> Unit
) {
    val mContext: Context = LocalContext.current
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mSurfaceColor: Color =
        if (MaterialTheme.colorScheme.isLight()) Color.Black else Color.Yellow
    val mSurfaceElevation: Dp = 4.dp
    val mCurrency: Currency by viewModelSettings.currentCurrency.collectAsStateWithLifecycle()

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

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier.shadow(
                elevation = mSurfaceElevation,
                ambientColor = mSurfaceColor,
                spotColor = mSurfaceColor,
            )
        ) {
            TopAppBar(
                title = { Text(text = if (!viewModel.isEditMode) "Add New Expense" else "Edit Expense") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            clear()
                            onPopBackStack()
                        }, modifier = Modifier.size(size = Constants.ICON_SIZE)
                    ) { BackIcon() }
                },
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            ExpenseAddEditMain(
                viewModel = viewModel,
                currency = mCurrency,
                focusManager = mFocusManager,
                focusRequester = mFocusRequester,
                keyboardController = mKeyboardController
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ExpenseAddEditMain(
    viewModel: ExpenseAddEditViewModel,
    currency: Currency,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {
    val mState by viewModel.uiState.collectAsState()
    var mDatePickerDialog: Boolean by rememberSaveable { mutableStateOf(value = false) }
    val mExpenseEntity: ExpenseEntity = mState.expense ?: ExpenseEntity()

    if (mDatePickerDialog) {
        DateTimePickerDialog(selectedDate = mExpenseEntity.date) {
            viewModel.onEvent(ExpenseAddEditEvent.OnDateChange(it))
        }
    }

    fun clear() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    mState.expense?.let { expenseEntity ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val mOptions: List<String> = listOf(Constants.TYPE_EXPENSE, Constants.TYPE_INCOME)
            MyTextField(value = HelperUtils.getDateTime(
                time = expenseEntity.date, pattern = Constants.PATTERN_TEXT_FIELD
            ),
                onValueChange = {},
                modifier = Modifier.onFocusChanged { mDatePickerDialog = it.isFocused },
                readOnly = true,
                label = { Text(stringResource(id = R.string.label_date)) },
                leadingIcon = { DateIcon() },
                trailingIcon = {},
                supportingText = {})

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
            ) {
                mOptions.forEach { text ->
                    val mType = if (text == Constants.TYPE_EXPENSE) {
                        Constants.TYPE_EXPENSE
                    } else Constants.TYPE_INCOME
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp)
                            .background(
                                color = if (text == expenseEntity.type) {
                                    MaterialTheme.colorScheme.primary
                                } else Color.LightGray
                            )
                            .clickable { viewModel.onEvent(ExpenseAddEditEvent.OnTypeChange(text)) }) {
                        Text(
                            text = mType.uppercase(),
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.White,
                            fontWeight = if (text == expenseEntity.type) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            DropDownTextField(items = CategoryModel.listCategories, onEvent = viewModel::onEvent)

            MyTextField(
                value = expenseEntity.amount,
                onValueChange = {
                    viewModel.onEvent(ExpenseAddEditEvent.OnAmountChange(it))
                },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            keyboardController?.show()
                        }
                    },
                label = { Text(stringResource(id = R.string.label_by_amount)) },
                leadingIcon = { CurrencyIcon() },
                trailingIcon = {
                    if (expenseEntity.amount.isNotEmpty()) {
                        CloseIcon(modifier = Modifier.clickable {
                            if (expenseEntity.amount.isNotEmpty()) {
                                viewModel.onEvent(ExpenseAddEditEvent.OnAmountChange(amount = ""))
                            }
                        })
                    }
                },
                prefix = { Text(text = currency.symbol) },
                supportingText = { },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )

            MyTextField(
                value = expenseEntity.description,
                onValueChange = {
                    if (it.length <= Constants.LEN_DESCRIPTION) {
                        viewModel.onEvent(ExpenseAddEditEvent.OnDescriptionChange(it))
                    }
                },
                label = { Text(stringResource(id = R.string.label_description)) },
                leadingIcon = { NotesIcon() },
                trailingIcon = {
                    if (expenseEntity.description.isNotEmpty()) {
                        CloseIcon(modifier = Modifier.clickable {
                            if (expenseEntity.description.isNotEmpty()) {
                                viewModel.onEvent(
                                    ExpenseAddEditEvent.OnDescriptionChange(description = "")
                                )
                            }
                        })
                    }
                },
                supportingText = {
                    Text(
                        text = "${expenseEntity.description.length} / ${Constants.LEN_DESCRIPTION}",
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
                    viewModel.onEvent(ExpenseAddEditEvent.OnSave)
                }, enabled = expenseEntity.amount.isNotEmpty()
            ) {
                Text(text = if (!viewModel.isEditMode) Constants.BUTTON_SAVE else Constants.BUTTON_UPDATE)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTextField(items: List<CategoryModel>, onEvent: (ExpenseAddEditEvent) -> Unit) {
    var isExpanded: Boolean by remember { mutableStateOf(value = false) }
    var mSelectedItem: String by remember { mutableStateOf(value = CategoryModel.others.title) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { expand -> isExpanded = expand },
    ) {
        MyTextField(
            value = mSelectedItem,
            onValueChange = {},
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            label = { Text(stringResource(id = R.string.label_category)) },
            leadingIcon = { NotesIcon() },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            items.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category.title) },
                    onClick = {
                        mSelectedItem = category.title
                        onEvent(ExpenseAddEditEvent.OnCategoryChange(mSelectedItem))
                        isExpanded = false
                    }
                )
            }
        }
    }
}