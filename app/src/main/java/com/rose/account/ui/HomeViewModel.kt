package com.rose.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rose.account.database.model.UserModel
import com.rose.account.database.repository.UsersRepository
import com.rose.account.preferences.PreferencesManager
import com.rose.account.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _preferences = preferencesManager.preferencesFlow
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText

    val getAllUsersWithFiltered: Flow<List<UserModel>> =
        combine(searchText, _preferences) { searchQuery, preferences ->
            Pair(searchQuery, preferences)
        }.flatMapLatest { (text, pref) ->
            usersRepository.getAllCustomersSortedByNames(text, pref.sortOrder)
        }

    fun getAllUsers(): Flow<List<UserModel>> = usersRepository.getAllCustomers

    fun userDelete(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.delete(userModel)
        }
    }

    fun userInsertOrUpdate(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            usersRepository.insertOrUpdate(userModel)
        }
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            preferencesManager.updateSortOrder(sortOrder)
        }
    }
}