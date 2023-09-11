package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.core.ResultState.Companion.flowMap
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getAllTransById(id: Int): Flow<ResultState<TransModel?>> {
        return flowMap {
            transDao.getAllTransById(id).map { transaction ->
                ResultState.Success(transaction)
            }
        }
    }

    override fun getAllTransByUserId(userId: Int): Flow<List<TransModel>> {
        return transDao.getAllTransByUserId(userId)
    }

    override fun getAllTransByUserIdWithSearch(
        userId: Int, searchQuery: String
    ): Flow<ResultState<List<TransModel>>> {
        return flowMap {
            transDao.getAllTransByUserIdWithSearch(userId, searchQuery).map { transactionsList ->
                ResultState.Success(transactionsList)
            }
        }
    }

    override fun getAccountBalanceByUser(userId: Int): Flow<ResultState<TransSumModel>> {
        return flowMap {
            transDao.getAccountBalanceByUser(userId).map { balance ->
                ResultState.Success(balance)
            }
        }
    }

    override fun getAllAccountsBalance(): Flow<TransSumModel> {
        return transDao.getAllAccountsBalance()
    }

}