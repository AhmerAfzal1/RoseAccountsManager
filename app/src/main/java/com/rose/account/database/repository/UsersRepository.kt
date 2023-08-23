package com.rose.account.database.repository

import androidx.annotation.WorkerThread
import com.rose.account.database.dao.UsersDao
import com.rose.account.database.model.UsersModel
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(private val mUsersDao: UsersDao) {

    @WorkerThread
    suspend fun insertOrUpdate(usersModel: UsersModel) = withContext(Dispatchers.IO) {
        mUsersDao.insertOrUpdate(usersModel)
    }

    @WorkerThread
    suspend fun delete(usersModel: UsersModel) = withContext(Dispatchers.IO) {
        mUsersDao.delete(usersModel)
    }

    fun getAllCustomersSortedByNames(
        searchName: String, sortOrder: SortOrder
    ): Flow<List<UsersModel>> = mUsersDao.getAllUsersByFilter(searchName, sortOrder)


    val getAllCustomers: Flow<List<UsersModel>> = mUsersDao.getAllUsers()

}

