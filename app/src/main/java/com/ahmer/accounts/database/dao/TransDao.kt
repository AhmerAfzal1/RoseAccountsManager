package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransDao {

    @Upsert
    suspend fun insertOrUpdate(transModel: TransModel)

    @Delete
    suspend fun delete(transModel: TransModel)

    @Query("SELECT * FROM Transactions WHERE _id =:id ORDER BY Created ASC")
    fun getAllTransById(id: Long): Flow<TransModel>

    @Query("SELECT * FROM Transactions WHERE UserID =:userId ORDER BY Created ASC")
    fun getAllTransByUserId(userId: Long): Flow<List<TransModel>>

    @Query("SELECT * FROM Transactions WHERE UserID = :userId AND Description LIKE '%' || :searchQuery || '%' ORDER BY Created ASC")
    fun getAllTransByUserIdWithSearch(userId: Long, searchQuery: String): Flow<List<TransModel>>

    @Transaction
    @Query("SELECT SUM(CASE WHEN Type = 'Credit' THEN Amount ELSE 0 END) AS creditSum, SUM(CASE WHEN Type = 'Debit' THEN Amount ELSE 0 END) AS debitSum FROM Transactions WHERE (Type IN('Credit', 'Debit') AND UserID = :userId)")
    fun getAccountBalanceByUser(userId: Long): Flow<TransSumModel>

    @Transaction
    @Query("SELECT SUM(CASE WHEN Type = 'Credit' THEN Amount ELSE 0 END) AS creditSum, SUM(CASE WHEN Type = 'Debit' THEN Amount ELSE 0 END) AS debitSum FROM Transactions WHERE Type IN('Credit', 'Debit')")
    fun getAllAccountsBalance(): Flow<TransSumModel>
}