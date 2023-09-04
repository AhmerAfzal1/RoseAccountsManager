package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun insertOrUpdate(userModel: UserModel)

    @Delete
    suspend fun delete(userModel: UserModel)

    @Query("SELECT * FROM customers WHERE _id = :id")
    suspend fun getUserById(id: Int): UserModel?

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
}