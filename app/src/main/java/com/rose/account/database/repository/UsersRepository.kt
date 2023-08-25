package com.rose.account.database.repository

import androidx.annotation.WorkerThread
import com.rose.account.database.dao.UsersDao
import com.rose.account.database.model.UserModel
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(private val usersDao: UsersDao) {

    @WorkerThread
    suspend fun insertOrUpdate(userModel: UserModel) = withContext(Dispatchers.IO) {
        usersDao.insertOrUpdate(userModel)
    }

    @WorkerThread
    suspend fun delete(userModel: UserModel) = withContext(Dispatchers.IO) {
        usersDao.delete(userModel)
    }

    @WorkerThread
    fun getAllCustomersSortedByNames(
        searchText: String, sortOrder: SortOrder
    ): Flow<List<UserModel>> = usersDao.getAllUsersByFilter(searchText, sortOrder)

    val getAllCustomers: Flow<List<UserModel>> = usersDao.getAllUsers()

}

