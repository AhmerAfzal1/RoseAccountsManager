package com.rose.account.database.repository

import androidx.annotation.WorkerThread
import com.rose.account.database.dao.UsersDao
import com.rose.account.database.model.UserModel
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(private val mUsersDao: UsersDao) {

    @WorkerThread
    suspend fun insertOrUpdate(userModel: UserModel) = withContext(Dispatchers.IO) {
        mUsersDao.insertOrUpdate(userModel)
    }

    @WorkerThread
    suspend fun delete(userModel: UserModel) = withContext(Dispatchers.IO) {
        mUsersDao.delete(userModel)
    }

    fun getAllCustomersSortedByNames(
        searchName: String, sortOrder: SortOrder
    ): Flow<List<UserModel>> = mUsersDao.getAllUsersByFilter(searchName, sortOrder)


    val getAllCustomers: Flow<List<UserModel>> = mUsersDao.getAllUsers()

}

