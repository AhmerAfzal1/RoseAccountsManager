package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

interface TransRepository {

    suspend fun insertOrUpdate(transEntity: TransEntity)

    suspend fun delete(transEntity: TransEntity)

    fun getAllTransById(transId: Int): Flow<ResultState<TransEntity?>>

    fun getAllTransByPersonId(personId: Int): Flow<List<TransEntity>>

    fun getAllTransByPersonIdForPdf(personId: Int): Flow<List<TransEntity>>

    fun getAllTransByPersonIdWithSearch(
        personId: Int, searchQuery: String
    ): Flow<ResultState<List<TransEntity>>>

    fun getAccountBalanceByPerson(personId: Int): Flow<ResultState<TransSumModel>>

    fun getAllAccountsBalance(): Flow<TransSumModel>
}