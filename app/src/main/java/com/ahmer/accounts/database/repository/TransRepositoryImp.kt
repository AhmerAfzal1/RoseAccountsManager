package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.entity.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class TransRepositoryImp @Inject constructor(private val transDao: TransDao) : TransRepository {
    override suspend fun insertOrUpdate(transEntity: TransEntity) {
        return withContext(Dispatchers.IO) {
            transDao.insertOrUpdate(transEntity = transEntity)
        }
    }

    override suspend fun delete(transEntity: TransEntity) {
        return withContext(Dispatchers.IO) {
            transDao.delete(transEntity = transEntity)
        }
    }

    override fun allTransactionsByPersonId(personId: Int, sort: Int): Flow<List<TransEntity>> {
        return transDao.allTransactionsByPersonId(personId = personId, sort = sort)
    }

    override fun allTransactionsByBetweenDates(dates: List<String>): Flow<List<TransEntity>> {
        return transDao.allTransactionsByBetweenDates(dates = dates)
    }

    override fun allTransactionsByDate(date: String): Flow<List<TransEntity>> {
        return transDao.allTransactionsByDate(date = date)
    }

    override fun allTransactionsSearch(
        personId: Int, searchQuery: String
    ): Flow<List<TransEntity>> {
        return transDao.allTransactionsSearch(personId = personId, searchQuery = searchQuery)
    }

    override fun allTransactions(): Flow<List<TransEntity>> {
        return transDao.allTransactions()
    }

    override fun transactionById(transId: Int): Flow<TransEntity?> {
        return transDao.transactionById(transId = transId)
    }

    override fun balanceByPerson(personId: Int): Flow<TransSumModel> {
        return transDao.balanceByPerson(personId = personId)
    }

    override fun balanceByAllAccounts(): Flow<TransSumModel> = transDao.balanceByAllAccounts()
}