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

    @Query("SELECT * FROM customers WHERE Name LIKE '%' || :searchText || '%' ORDER BY Name ASC")
    fun getAllUsersSortedByNames(searchText: String): Flow<List<UserModel>>

    @Query("SELECT * FROM customers WHERE Name LIKE '%' || :searchText || '%' ORDER BY Created ASC")
    fun getAllUsersSortedByDate(searchText: String): Flow<List<UserModel>>

    fun getAllUsersByFilter(searchText: String, sortOrder: SortOrder): Flow<List<UserModel>> {
        return when (sortOrder) {
            SortOrder.BY_NAME -> getAllUsersSortedByNames(searchText)
            SortOrder.BY_DATE -> getAllUsersSortedByDate(searchText)
        }
    }

    @Query("SELECT * FROM customers")
    fun getAllUsers(): Flow<List<UserModel>>
}