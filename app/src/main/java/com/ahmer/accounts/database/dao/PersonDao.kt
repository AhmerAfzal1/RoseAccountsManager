package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Upsert
    suspend fun insertOrUpdate(personsEntity: PersonsEntity)

    @Delete
    suspend fun delete(personsEntity: PersonsEntity)

    @Query("SELECT * FROM Persons")
    fun getAllPersons(): Flow<List<PersonsEntity>>

    @Query("SELECT * FROM Persons WHERE name LIKE '%' || :searchQuery || '%' ORDER BY created ASC")
    fun getAllPersonsSortedByDate(searchQuery: String): Flow<List<PersonsEntity>>

    @Query("SELECT * FROM Persons WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun getAllPersonsSortedByName(searchQuery: String): Flow<List<PersonsEntity>>

    fun getAllPersonsByFilter(
        searchQuery: String, sortOrder: SortOrder
    ): Flow<List<PersonsEntity>> = when (sortOrder) {
        SortOrder.Date -> getAllPersonsSortedByDate(searchQuery)
        SortOrder.Name -> getAllPersonsSortedByName(searchQuery)
    }

    @Query("SELECT * FROM Persons WHERE id = :personId")
    fun getPersonById(personId: Int): Flow<PersonsEntity?>

    @Transaction
    @Query("SELECT SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum, SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum FROM Transactions WHERE (type IN('Credit', 'Debit') AND personId = :personId)")
    fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel>

    @Transaction
    @Query("SELECT SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum, SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum FROM Transactions WHERE (type IN('Credit', 'Debit'))")
    fun getAllAccountsBalance(): Flow<TransSumModel>
}