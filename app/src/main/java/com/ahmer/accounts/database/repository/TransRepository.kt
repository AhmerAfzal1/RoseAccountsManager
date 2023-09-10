package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

interface TransRepository {

    suspend fun insertOrUpdate(transModel: TransModel)

    suspend fun delete(transModel: TransModel)

    fun getAllTransById(id: Long): Flow<ResultState<TransModel?>>

    fun getAllTransByUserId(userId: Long): Flow<List<TransModel>>

    fun getAllTransByUserIdWithSearch(
        userId: Long, searchQuery: String
    ): Flow<ResultState<List<TransModel>>>

    fun getAccountBalanceByUser(userId: Long): Flow<ResultState<TransSumModel>>

    fun getAllAccountsBalance(): Flow<TransSumModel>
}