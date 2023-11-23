package com.ahmer.accounts.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.PersonState
import com.ahmer.accounts.ui.components.PersonItem
import com.ahmer.accounts.ui.components.TotalBalance
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.SearchIcon
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.SortIcon
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonsListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit,
    personViewModel: PersonViewModel,
    settingsViewModel: SettingsViewModel,
    transSumModel: TransSumModel,
) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCurrentCurrency: Currency by settingsViewModel.currentCurrency.collectAsStateWithLifecycle()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mState: PersonState by personViewModel.uiState.collectAsStateWithLifecycle()
    var isVisibleFab: Boolean by rememberSaveable { mutableStateOf(value = true) }
    var mTextSearch: String by remember { mutableStateOf(value = personViewModel.searchQuery.value) }

    LaunchedEffect(key1 = true) {
        personViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    /*
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (mResult == SnackbarResult.ActionPerformed) { }
                    */
                }

                is UiEvent.RelaunchApp -> HelperUtils.relaunchApp(context = mContext)
                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisibleFab,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(onClick = { personViewModel.onEvent(PersonEvent.OnNewAddClick) }) {
                    AddIcon()
                }
            }
        },
    ) { innerPadding ->
        val mNestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y < -1) isVisibleFab = false // Hide FAB
                    if (available.y > 1) isVisibleFab = true // Show FAB
                    return Offset.Zero
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
        ) {
            TotalBalance(
                transSumModel = transSumModel,
                currency = mCurrentCurrency,
                modifier = Modifier.padding(all = 5.dp),
            )
            SearchBarPerson(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp),
                text = mTextSearch,
                onTextChange = { text ->
                    personViewModel.onEvent(PersonEvent.OnSearchTextChange(text))
                    mTextSearch = text
                },
                viewModel = personViewModel,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding)
                    .nestedScroll(connection = mNestedScrollConnection),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = mState.allPersons,
                    key = { persons -> persons.personsEntity.id },
                ) { person ->
                    PersonItem(
                        personsBalanceModel = person,
                        currency = mCurrentCurrency,
                        onEvent = personViewModel::onEvent,
                        modifier = Modifier
                            .animateItemPlacement(
                                animationSpec = tween(durationMillis = Constants.ANIMATE_ITEM_DURATION)
                            )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarPerson(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    viewModel: PersonViewModel,
) = Box(modifier = modifier) {
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val isFocused: Boolean by mInteractionSource.collectIsFocusedAsState()
    var mShowBottomSheet: Boolean by remember { mutableStateOf(value = false) }

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(height = 40.dp)
                .padding(start = 8.dp, end = 8.dp)
                .weight(weight = 0.90f)
                .border(
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(percent = 50)
                )
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 56.dp),
                placeholder = { Text(stringResource(id = R.string.label_search)) },
                leadingIcon = { SearchIcon() },
                trailingIcon = {
                    if (isFocused) {
                        CloseIcon(modifier = Modifier.clickable {
                            mCoroutineScope.launch { delay(duration = 200.milliseconds) }
                            if (text.isNotEmpty()) onTextChange("") else mFocusManager.clearFocus()
                        })
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onTextChange(text)
                    mKeyboardController?.hide()
                    mFocusManager.clearFocus()
                }),
                singleLine = true,
                maxLines = 1,
                interactionSource = mInteractionSource,
                shape = RoundedCornerShape(size = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
        Box(
            modifier = Modifier.weight(weight = 0.10f), contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = { mShowBottomSheet = !mShowBottomSheet }) { SortIcon() }

            val mCurrentSortOrder by viewModel.currentSortOrder.collectAsStateWithLifecycle()
            val mSheetState: SheetState = rememberModalBottomSheetState()

            if (mShowBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { mShowBottomSheet = false },
                    modifier = Modifier.fillMaxHeight(fraction = 0.4f),
                    sheetState = mSheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },
                ) {
                    Text(
                        text = stringResource(R.string.label_bottom_sheet_sort_filter),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )

                    OrderSection(modifier = Modifier,
                        sortOrder = mCurrentSortOrder,
                        onOrderChange = { viewModel.updateSortOrder(sortOrder = it) })
                }
            }
        }
    }
}

@Composable
private fun MyRadioButton(
    title: String, selected: Boolean, onSelect: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onSelect,
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected, onClick = onSelect, colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onBackground
            )
        )
        Text(text = title)
    }
}

@Composable
private fun OrderSection(
    modifier: Modifier = Modifier,
    sortOrder: SortOrder = SortOrder.Date(SortBy.Descending),
    onOrderChange: (SortOrder) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.label_by_sort),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                MyRadioButton(title = stringResource(R.string.label_by_recent),
                    selected = sortOrder is SortOrder.Date,
                    onSelect = { onOrderChange(SortOrder.Date(sortOrder.sortBy)) })
            }
            item {
                MyRadioButton(title = stringResource(R.string.label_by_name),
                    selected = sortOrder is SortOrder.Name,
                    onSelect = { onOrderChange(SortOrder.Name(sortOrder.sortBy)) })
            }
            item {
                MyRadioButton(title = stringResource(R.string.label_by_amount),
                    selected = sortOrder is SortOrder.Amount,
                    onSelect = { onOrderChange(SortOrder.Amount(sortOrder.sortBy)) })
            }
        }

        Text(
            text = stringResource(R.string.label_by_sort_order),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                MyRadioButton(title = stringResource(R.string.label_by_ascending),
                    selected = sortOrder.sortBy is SortBy.Ascending,
                    onSelect = { onOrderChange(sortOrder.copy(SortBy.Ascending)) })
            }
            item {
                MyRadioButton(title = stringResource(R.string.label_by_descending),
                    selected = sortOrder.sortBy is SortBy.Descending,
                    onSelect = { onOrderChange(sortOrder.copy(SortBy.Descending)) })
            }
        }
    }
}