package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class TransRepositoryImp @Inject constructor(private val transDao: TransDao) : TransRepository {

    override suspend fun insertOrUpdate(transModel: TransModel) = withContext(Dispatchers.IO) {
        transDao.insertOrUpdate(transModel)
    }

    override suspend fun delete(transModel: TransModel) = withContext(Dispatchers.IO) {
        transDao.delete(transModel)
    }

    override fun getAllTransByUserId(userId: Long): Flow<List<TransModel>> {
        return transDao.getAllTransByUserId(userId)
    }

    override fun getAllTransByUserIdWithSearch(
        userId: Long, searchQuery: String
    ): Flow<List<TransModel>> {
        return transDao.getAllTransByUserIdWithSearch(userId, searchQuery)
    }

    override fun getAccountBalanceByUser(userId: Long): Flow<TransSumModel> {
        return transDao.getAccountBalanceByUser(userId)
    }

    override fun getAllAccountsBalance(): Flow<TransSumModel> {
        return transDao.getAllAccountsBalance()
    }

}