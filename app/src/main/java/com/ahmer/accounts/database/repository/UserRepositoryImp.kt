package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.core.ResultState.Companion.flowMap
import com.ahmer.accounts.database.dao.UserDao
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class UserRepositoryImp @Inject constructor(private val userDao: UserDao) : UserRepository {

    override suspend fun insertOrUpdate(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdate(userModel)
    }

    override suspend fun delete(userModel: UserModel) = withContext(Dispatchers.IO) {
        userDao.delete(userModel)
    }

    override fun getUserById(id: Int): Flow<ResultState<UserModel?>> {
        return flowMap {
            userDao.getUserById(id).map { user ->
                ResultState.Success(user)
            }
        }
    }

    override fun getAllUsers(): Flow<List<UserModel>> = userDao.getAllUsers()

    override fun getAllUsersBySearchAndSort(
        searchQuery: String, sortBy: SortBy
    ): Flow<ResultState<List<UserModel>>> {
        return flowMap {
            userDao.getAllUsersBySearchAndSort(searchQuery, sortBy).map { userList ->
                ResultState.Success(userList)
            }
        }
    }

    override fun getAccountBalanceByUser(userId: Int): Flow<TransSumModel> {
        return userDao.getAccountBalanceByUser(userId)
    }

    override fun getAllAccountsBalance(): Flow<TransSumModel> {
        return userDao.getAllAccountsBalance()
    }
}