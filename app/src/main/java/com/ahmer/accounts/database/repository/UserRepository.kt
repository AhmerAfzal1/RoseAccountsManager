package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.UserDao
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insertOrUpdate(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdate(userModel)
    }

    suspend fun delete(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.delete(userModel)
    }

    fun getAllUsers(): Flow<List<UserModel>> = userDao.getAllUsers()

    fun getAllUsersBySearchAndSort(
        searchQuery: String, sortBy: SortBy
    ): Flow<List<UserModel>> = userDao.getAllUsersBySearchAndSort(searchQuery, sortBy)

    fun getPinnedUsers(id: Int): Flow<UserModel> = userDao.getPinnedUsers(id)
}