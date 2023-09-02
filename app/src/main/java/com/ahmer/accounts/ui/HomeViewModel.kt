package com.ahmer.accounts.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.event.HomeEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.event.UserState
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.preferences.PreferencesFilter
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.usecase.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UserUseCase,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _preferences: Flow<PreferencesFilter> = preferencesManager.preferencesFlow

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private var mDeletedUser: UserModel? = null

    private val _state: MutableState<UserState> = mutableStateOf(UserState())
    val state: MutableState<UserState> = _state

    private var userJob: Job? = null

    private fun getAllUsers() {
        userJob?.cancel()
        userJob = useCase.getAllUsersUseCase(_searchQuery, _preferences).onEach { userData ->
            _state.value = state.value.copy(getAllUsersList = userData)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedUser = event.userModel
                    useCase.deleteUserUseCase(event.userModel)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "User ${event.userModel.name} deleted", action = "Undo"
                        )
                    )
                }
            }

            is HomeEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Navigate(route = ScreenRoutes.AddEditScreen + "?userId=${event.userModel.id}"))
                }
            }

            is HomeEvent.OnSortBy -> {
                viewModelScope.launch {
                    preferencesManager.updateSortOrder(event.sortBy)
                }
            }

            HomeEvent.OnAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Navigate(ScreenRoutes.AddEditScreen))
                }
            }

            HomeEvent.OnUndoDeleteClick -> {
                mDeletedUser?.let { user ->
                    viewModelScope.launch {
                        useCase.addUserUseCase(user)
                    }
                }
            }
        }
    }

    init {
        getAllUsers()
    }
}