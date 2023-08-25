package com.rose.account.database.repository

import com.rose.account.database.model.UserModel
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertOrUpdate(userModel: UserModel)
    suspend fun delete(userModel: UserModel)
    fun getAllUsersData(): Flow<List<UserModel>>
    fun getAllUsersDataFilter(searchText: String, sortOrder: SortOrder): Flow<List<UserModel>>
}