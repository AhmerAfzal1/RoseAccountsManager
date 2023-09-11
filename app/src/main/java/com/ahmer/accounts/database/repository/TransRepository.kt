package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

interface TransRepository {

    suspend fun insertOrUpdate(transModel: TransModel)

    suspend fun delete(transModel: TransModel)

    fun getAllTransById(id: Int): Flow<ResultState<TransModel?>>

    fun getAllTransByUserId(userId: Int): Flow<List<TransModel>>

    fun getAllTransByUserIdWithSearch(
        userId: Int, searchQuery: String
    ): Flow<ResultState<List<TransModel>>>

    fun getAccountBalanceByUser(userId: Int): Flow<ResultState<TransSumModel>>

    fun getAllAccountsBalance(): Flow<TransSumModel>
}