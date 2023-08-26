package com.rose.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rose.account.database.model.UserModel
import com.rose.account.database.repository.UserRepository
import com.rose.account.database.state.HomeUiState
import com.rose.account.database.state.Result
import com.rose.account.database.state.UiState
import com.rose.account.database.state.asResult
import com.rose.account.preferences.PreferencesManager
import com.rose.account.utils.SortOrder
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

    val getUserUiState: StateFlow<HomeUiState> = combine(
        getAllUsersBySearchAndSort, isRefreshing, isError
    ) { userData, refreshing, error ->
        val userModel: UiState = when (userData) {
            is Result.Success -> UiState.Success(userData.data)
            is Result.Error -> UiState.Error
            Result.Loading -> UiState.Loading
        }

        HomeUiState(userModel, refreshing, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(UiState.Loading, isRefreshing = false, isError = false)
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