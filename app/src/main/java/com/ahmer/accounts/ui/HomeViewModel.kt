package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.core.event.HomeEvent
import com.ahmer.accounts.core.event.UiEvent
import com.ahmer.accounts.core.state.UserState
import com.ahmer.accounts.database.model.UserModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val preferencesManager: PreferencesManager,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _preferences: Flow<PreferencesFilter> = preferencesManager.preferencesFlow

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(UserState())
    val uiState = _uiState.asStateFlow()

    private var mDeletedUser: UserModel? = null
    private var mLoadUsersJob: Job? = null

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedUser = event.userModel
                    userUseCase.deleteUser(event.userModel)
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
                        userUseCase.addUser(user)
                    }
                }
            }
        }
    }

    fun getAllUsersData() {
        mLoadUsersJob?.cancel()
        mLoadUsersJob =
            userUseCase.getAllUsers(_searchQuery, _preferences).onEach { resultState ->
                _uiState.update { userState -> userState.copy(getAllUsersList = resultState) }
            }.launchIn(viewModelScope)
    }

    init {
        getAllUsersData()
    }
}