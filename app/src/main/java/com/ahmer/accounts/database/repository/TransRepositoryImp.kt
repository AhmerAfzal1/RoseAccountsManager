package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.core.ResultState.Companion.flowMap
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class TransRepositoryImp @Inject constructor(private val transDao: TransDao) : TransRepository {

    override suspend fun insertOrUpdate(transEntity: TransEntity) = withContext(Dispatchers.IO) {
        transDao.insertOrUpdate(transEntity)
    }

    override suspend fun delete(transEntity: TransEntity) = withContext(Dispatchers.IO) {
        transDao.delete(transEntity)
    }

    override fun getAllTransById(transId: Int): Flow<ResultState<TransEntity?>> {
        return flowMap {
            transDao.getAllTransById(transId).map { transaction ->
                ResultState.Success(transaction)
            }
        }
    }

    override fun getAllTransByPersonId(personId: Int): Flow<List<TransEntity>> {
        return transDao.getAllTransByPersonId(personId)
    }

    override fun getAllTransByPersonIdForPdf(personId: Int): Flow<List<TransEntity>> {
        return transDao.getAllTransByPersonIdForPdf(personId)
    }

    override fun getAllTransByPersonIdWithSearch(
        personId: Int, searchQuery: String
    ): Flow<ResultState<List<TransEntity>>> {
        return flowMap {
            transDao.getAllTransByPersonIdWithSearch(personId, searchQuery).map { transList ->
                ResultState.Success(transList)
            }
        }
    }

    override fun getAccountBalanceByPerson(personId: Int): Flow<ResultState<TransSumModel>> {
        return flowMap {
            transDao.getAccountBalanceByPerson(personId).map { balance ->
                ResultState.Success(balance)
            }
        }
    }

    override fun getAllAccountsBalance(): Flow<TransSumModel> = transDao.getAllAccountsBalance()

}