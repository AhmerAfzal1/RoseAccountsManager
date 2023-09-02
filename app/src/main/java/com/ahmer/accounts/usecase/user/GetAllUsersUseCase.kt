package com.ahmer.accounts.usecase.user

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.preferences.PreferencesFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

class GetAllUsersUseCase(private val repository: UserRepository) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        searchQuery: MutableStateFlow<String>, preferences: Flow<PreferencesFilter>
    ): Flow<List<UserModel>> = combine(searchQuery, preferences) { query, preference ->
        Pair(query, preference)
    }.flatMapLatest { (search, pref) ->
        repository.getAllUsersBySearchAndSort(search, pref.sortBy)
    }
}