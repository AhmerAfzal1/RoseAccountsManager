package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun insertOrUpdate(userModel: UserModel)

    @Delete
    suspend fun delete(userModel: UserModel)

    @Query("SELECT * FROM customers")
    fun getAllUsers(): Flow<List<UserModel>>

    @Query("SELECT * FROM customers WHERE Name LIKE '%' || :searchQuery || '%' ORDER BY Created ASC")
    fun getAllUsersSortedByDate(searchQuery: String): Flow<List<UserModel>>

    @Query("SELECT * FROM customers WHERE Name LIKE '%' || :searchQuery || '%' ORDER BY Name ASC")
    fun getAllUsersSortedByNames(searchQuery: String): Flow<List<UserModel>>

    fun getAllUsersBySearchAndSort(searchQuery: String, sortBy: SortBy): Flow<List<UserModel>> {
        return when (sortBy) {
            SortBy.DATE -> getAllUsersSortedByDate(searchQuery)
            SortBy.NAME -> getAllUsersSortedByNames(searchQuery)
        }
    }

    @Query("SELECT * FROM customers WHERE _id = :id")
    fun getUserById(id: Int): Flow<UserModel?>

    @Transaction
    @Query("SELECT SUM(CASE WHEN Type = 'Credit' THEN Amount ELSE 0 END) AS creditSum, SUM(CASE WHEN Type = 'Debit' THEN Amount ELSE 0 END) AS debitSum FROM Transactions WHERE (Type IN('Credit', 'Debit') AND UserID = :userId)")
    fun getAccountBalanceByUser(userId: Int): Flow<TransSumModel>

    @Transaction
    @Query("SELECT SUM(CASE WHEN Type = 'Credit' THEN Amount ELSE 0 END) AS creditSum, SUM(CASE WHEN Type = 'Debit' THEN Amount ELSE 0 END) AS debitSum FROM Transactions WHERE Type IN('Credit', 'Debit')")
    fun getAllAccountsBalance(): Flow<TransSumModel>
}