package com.ahmer.accounts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.database.state.Result
import com.ahmer.accounts.database.state.UiState
import com.ahmer.accounts.database.state.UiStateEvent
import com.ahmer.accounts.database.state.asResult
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

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
            repository.getAllUsersBySearchAndSort(search, preference.sortOrder)
        }.asResult()

    val getUserUiState: StateFlow<UiStateEvent> = combine(
        getAllUsersBySearchAndSort, isRefreshing, isError
    ) { userData, refreshing, error ->
        val userModel: UiState = when (userData) {
            is Result.Success -> UiState.Success(userData.data)
            is Result.Error -> UiState.Error
            Result.Loading -> UiState.Loading
        }

        UiStateEvent(userModel, refreshing, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = UiStateEvent(UiState.Loading, isRefreshing = false, isError = false)
    )

    fun insertOrUpdateUser(userModel: UserModel) = viewModelScope.launch {
        repository.insertOrUpdate(userModel)
    }

    fun deleteUser(userModel: UserModel) = viewModelScope.launch {
        repository.delete(userModel)
    }

    fun updateSortOrder(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

}