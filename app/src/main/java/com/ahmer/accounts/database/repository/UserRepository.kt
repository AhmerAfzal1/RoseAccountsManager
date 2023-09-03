package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.state.ResultState
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertOrUpdate(userModel: UserModel)

    suspend fun delete(userModel: UserModel)

    suspend fun getUserById(id: Int): UserModel?

    fun getAllUsers(): Flow<List<UserModel>>

    fun getAllUsersBySearchAndSort(
        searchQuery: String, sortBy: SortBy
    ): Flow<ResultState<List<UserModel>>>
}