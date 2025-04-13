package com.ahmer.accounts.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.event.PersonAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.PersonAddEditState
import com.ahmer.accounts.utils.AddressIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.EmailIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MyTextField
import com.ahmer.accounts.utils.NotesIcon
import com.ahmer.accounts.utils.PersonIcon
import com.ahmer.accounts.utils.PhoneIcon
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonAddEditScreen(viewModel: PersonAddEditViewModel, onPopBackStack: () -> Unit) {

    val uiState: PersonAddEditState by viewModel.uiState.collectAsState()

    val isLightTheme: Boolean = MaterialTheme.colorScheme.isLight()
    val context: Context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current
    val focusRequester: FocusRequester = remember { FocusRequester() }
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val shadowColor: Color = if (isLightTheme) Color.Black else Color.Yellow

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = context, msg = event.message
                )

                else -> Unit
            }
        }
    }

    fun clear() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RectangleShape,
                    ambientColor = shadowColor,
                    spotColor = shadowColor
                ),
            color = MaterialTheme.colorScheme.surface
        ) {
            TopAppBar(
                title = { Text(text = viewModel.titleBar.uppercase()) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            clear()
                            onPopBackStack()
                        },
                        modifier = Modifier.size(size = Constants.ICON_SIZE)
                    ) { BackIcon() }
                },
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.person?.let { person ->
                PersonInputField(
                    modifier = Modifier
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged { focus ->
                            if (focus.isFocused) {
                                keyboardController?.show()
                            }
                        },
                    value = person.name,
                    onValueChange = { viewModel.onEvent(PersonAddEditEvent.OnNameChange(it)) },
                    label = R.string.label_name,
                    icon = { PersonIcon() },
                    maxLength = Constants.LEN_NAME,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )

                PersonInputField(
                    value = person.phone,
                    onValueChange = { viewModel.onEvent(PersonAddEditEvent.OnPhoneChange(it)) },
                    label = R.string.label_phone_number,
                    icon = { PhoneIcon() },
                    maxLength = Constants.LEN_PHONE,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )

                ExpandableFieldsSection(
                    person = person,
                    onEvent = viewModel::onEvent,
                    focusManager = focusManager,
                    keyboardController = keyboardController
                )

                OutlinedButton(
                    onClick = {
                        clear()
                        viewModel.onEvent(PersonAddEditEvent.OnSaveClick)
                    },
                    enabled = person.name.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.label_save).uppercase())
                }
            }
        }
    }
}

@Composable
private fun ExpandableFieldsSection(
    person: PersonEntity,
    onEvent: (PersonAddEditEvent) -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {
    var expanded by remember { mutableStateOf(false) }

    if (!expanded) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.label_add_more_data).uppercase())
        }
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PersonInputField(
            value = person.email,
            onValueChange = { onEvent(PersonAddEditEvent.OnEmailChange(it)) },
            label = R.string.label_email,
            icon = { EmailIcon() },
            maxLength = Constants.LEN_EMAIL,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )

        PersonInputField(
            value = person.address,
            onValueChange = { onEvent(PersonAddEditEvent.OnAddressChange(it)) },
            label = R.string.label_address,
            icon = { AddressIcon() },
            maxLength = Constants.LEN_ADDRESS,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )

        PersonInputField(
            value = person.notes,
            onValueChange = { onEvent(PersonAddEditEvent.OnNotesChange(it)) },
            label = R.string.label_notes,
            icon = { NotesIcon() },
            maxLength = Constants.LEN_NOTES,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }),
        )
    }
}

@Composable
private fun PersonInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    icon: @Composable () -> Unit,
    maxLength: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        MyTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            modifier = modifier.fillMaxWidth(),
            label = { Text(stringResource(id = label)) },
            leadingIcon = icon,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    CloseIcon(Modifier.clickable { onValueChange("") })
                }
            },
            supportingText = {
                Text(
                    text = "${value.length}/$maxLength",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textColor = textColor
        )
    }
}