package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.event.UserEvent
import com.ahmer.accounts.event.UserUiEvent
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.preferences.PreferencesFilter
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UserUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UserUiEvent> = _eventFlow.asSharedFlow()

    private val _preferences: Flow<PreferencesFilter> = preferencesManager.preferencesFlow

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(UserState())
    val uiState = _uiState.asStateFlow()

    private var mDeletedUser: UserModel? = null
    private var mLoadUsersJob: Job? = null

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedUser = event.userModel
                    repository.delete(event.userModel)
                    _eventFlow.emit(
                        UserUiEvent.ShowSnackBar(
                            message = "User ${event.userModel.name} deleted", action = "Undo"
                        )
                    )
                }
            }

            is UserEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UserUiEvent.Navigate(route = ScreenRoutes.AddEditScreen + "?userId=${event.userModel.id}"))
                }
            }

            is UserEvent.OnSortBy -> {
                viewModelScope.launch {
                    preferencesManager.updateSortOrder(event.sortBy)
                }
            }

            UserEvent.OnAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UserUiEvent.Navigate(ScreenRoutes.AddEditScreen))
                }
            }

            UserEvent.OnUndoDeleteClick -> {
                mDeletedUser?.let { user ->
                    viewModelScope.launch {
                        repository.insertOrUpdate(user)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAllUsersBySearchAndSort(
        searchQuery: MutableStateFlow<String>, preferences: Flow<PreferencesFilter>
    ): Flow<ResultState<List<UserModel>>> = combine(searchQuery, preferences) { query, preference ->
        Pair(query, preference)
    }.flatMapLatest { (search, pref) ->
        repository.getAllUsersBySearchAndSort(search, pref.sortBy)
    }

    fun getAllUsersData() {
        mLoadUsersJob?.cancel()
        mLoadUsersJob =
            getAllUsersBySearchAndSort(_searchQuery, _preferences).onEach { resultState ->
                _uiState.update { userState -> userState.copy(getAllUsersList = resultState) }
            }.launchIn(viewModelScope)
    }

    init {
        getAllUsersData()
    }
}