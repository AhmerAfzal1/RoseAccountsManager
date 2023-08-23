package com.rose.account.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rose.account.database.model.UsersModel
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Upsert
    suspend fun insertOrUpdate(usersModel: UsersModel)

    @Delete
    suspend fun delete(usersModel: UsersModel)

    @Query("SELECT * FROM customers WHERE Name LIKE '%' || :searchName || '%' ORDER BY Name ASC")
    fun getAllUsersSortedByNames(searchName: String): Flow<List<UsersModel>>

    @Query("SELECT * FROM customers WHERE Name LIKE '%' || :searchName || '%' ORDER BY Created ASC")
    fun getAllUsersSortedByDate(searchName: String): Flow<List<UsersModel>>

    fun getAllUsersByFilter(searchName: String, sortOrder: SortOrder): Flow<List<UsersModel>> {
        return when (sortOrder) {
            SortOrder.BY_NAME -> getAllUsersSortedByNames(searchName)
            SortOrder.BY_DATE -> getAllUsersSortedByDate(searchName)
        }
    }

    @Query("SELECT * FROM customers")
    fun getAllUsers(): Flow<List<UsersModel>>
}