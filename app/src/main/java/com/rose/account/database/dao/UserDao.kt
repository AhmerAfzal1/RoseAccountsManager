package com.rose.account.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rose.account.database.model.UserModel
import com.rose.account.utils.SortOrder
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

    fun getAllUsersBySearchAndSort(
        searchQuery: String, sortOrder: SortOrder
    ): Flow<List<UserModel>> = when (sortOrder) {
        SortOrder.BY_DATE -> getAllUsersSortedByDate(searchQuery)
        SortOrder.BY_NAME -> getAllUsersSortedByNames(searchQuery)
    }

    @Query("SELECT * FROM customers WHERE _id=:id")
    fun getPinnedUsers(id: Int): Flow<UserModel>
}