package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

interface TransRepository {
    suspend fun insertOrUpdate(transEntity: TransEntity)
    suspend fun delete(transEntity: TransEntity)
    fun transactionById(transId: Int): Flow<TransEntity?>

    /**
     * @param personId id of person
     * @param sort sort order 0 for created and 1 for date
     */
    fun allTransactionByPersonId(personId: Int, sort: Int): Flow<List<TransEntity>>
    fun allTransactionsSearch(personId: Int, searchQuery: String): Flow<List<TransEntity>>
    fun balanceByPerson(personId: Int): Flow<TransSumModel>
    fun accountsBalance(): Flow<TransSumModel>
}