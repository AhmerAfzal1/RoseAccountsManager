package com.rose.account.database.repository

import androidx.annotation.WorkerThread
import com.rose.account.database.dao.UserDao
import com.rose.account.database.model.UserModel
import com.rose.account.utils.SortOrder
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class UserRepository @Inject constructor(private val userDao: UserDao) {

    @WorkerThread
    suspend fun insertOrUpdate(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdate(userModel)
    }

    @WorkerThread
    suspend fun delete(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.delete(userModel)
    }

    fun getAllUsers(): Flow<List<UserModel>> = userDao.getAllUsers()

    fun getAllUsersBySearchAndSort(
        searchQuery: String, sortOrder: SortOrder
    ): Flow<List<UserModel>> = userDao.getAllUsersBySearchAndSort(searchQuery, sortOrder)

    fun getPinnedUsers(id: Int): Flow<UserModel> = userDao.getPinnedUsers(id)
}