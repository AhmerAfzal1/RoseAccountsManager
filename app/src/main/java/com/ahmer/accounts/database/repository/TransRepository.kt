package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

interface TransRepository {

    suspend fun insertOrUpdate(transModel: TransModel)

    suspend fun delete(transModel: TransModel)

    fun getAllTransByUserId(userId: Long): Flow<List<TransModel>>

    fun getAllTransByUserIdWithSearch(userId: Long, searchQuery: String): Flow<List<TransModel>>

    fun getAccountBalanceByUser(userId: Long): Flow<TransSumModel>

    fun getAllAccountsBalance(): Flow<TransSumModel>
}