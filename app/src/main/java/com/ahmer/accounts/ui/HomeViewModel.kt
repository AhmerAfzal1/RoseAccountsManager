package com.ahmer.accounts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepositoryImp
import com.ahmer.accounts.event.HomeEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepositoryImp,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _preferences = preferencesManager.preferencesFlow

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var mDeletedUser: UserModel? = null

    val getAllUsersBySearchAndSort: Flow<List<UserModel>> =
        combine(_searchQuery, _preferences) { searchQuery, preferences ->
            Pair(searchQuery, preferences)
        }.flatMapLatest { (search, preference) ->
            repository.getAllUsersBySearchAndSort(search, preference.sortBy)
        }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedUser = event.userModel
                    repository.delete(event.userModel)
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = "User ${event.userModel.name} deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is HomeEvent.OnItemClick -> {
                sendUiEvent(UiEvent.Navigate(route = ScreenRoutes.AddEditScreen + "?userId=${event.userModel.id}"))
            }

            is HomeEvent.OnSortBy -> {
                viewModelScope.launch {
                    preferencesManager.updateSortOrder(event.sortBy)
                }
            }

            HomeEvent.OnAddClick -> {
                sendUiEvent(UiEvent.Navigate(ScreenRoutes.AddEditScreen))
            }

            HomeEvent.OnUndoDeleteClick -> {
                mDeletedUser?.let { user ->
                    viewModelScope.launch {
                        repository.insertOrUpdate(user)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    /*private val mExceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            isError.emit(true)
        }
    }
    private val _preferences = preferencesManager.preferencesFlow

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: MutableStateFlow<String> = _searchQuery

    private val isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val getAllUsers: Flow<Result<List<UserModel>>> = repository.getAllUsers().asResult()

    private val getAllUsersBySearchAndSort: Flow<Result<List<UserModel>>> =
        combine(_searchQuery, _preferences) { searchQuery, preferences ->
            Pair(searchQuery, preferences)
        }.flatMapLatest { (search, preference) ->
            repository.getAllUsersBySearchAndSort(search, preference.sortBy)
        }.asResult()

    val uiState: StateFlow<HomeUiState> = combine(
        getAllUsersBySearchAndSort, isRefreshing, isError
    ) { userData, refreshing, error ->
        val mUserModelUi: UiState = when (userData) {
            is Result.Success -> UiState.Success(userData.data)
            is Result.Error -> UiState.Error
            Result.Loading -> UiState.Loading
        }

        HomeUiState(mUserModelUi, refreshing, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        initialValue = HomeUiState(UiState.Loading, isRefreshing = false, isError = false)
    )

    fun onRefresh() {
        viewModelScope.launch(mExceptionHandler) {
            val mGetUserModelData = async { uiState }
            isRefreshing.emit(true)
            try {
                awaitAll(mGetUserModelData)
            } finally {
                isRefreshing.emit(false)
            }
        }
    }

    fun onErrorConsumed() = viewModelScope.launch {
        isError.emit(false)
    }

    fun deleteUser(userModel: UserModel) = viewModelScope.launch {
        repository.delete(userModel)
    }

    fun updateSortOrder(sortBy: SortBy) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortBy)
    }*/

}