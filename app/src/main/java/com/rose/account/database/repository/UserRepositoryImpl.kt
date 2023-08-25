package com.rose.account.database.repository

import androidx.annotation.WorkerThread
import com.rose.account.database.dao.UserDao
import com.rose.account.database.model.UserModel
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) :
    UserRepository {

    @WorkerThread
    override suspend fun insertOrUpdate(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdate(userModel)
    }

    @WorkerThread
    override suspend fun delete(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.delete(userModel)
    }

    override fun getAllUsersData(): Flow<List<UserModel>> = userDao.getAllUsers()

    override fun getAllUsersDataFilter(
        searchText: String, sortOrder: SortOrder
    ): Flow<List<UserModel>> {
        TODO("Not yet implemented")
    }

}