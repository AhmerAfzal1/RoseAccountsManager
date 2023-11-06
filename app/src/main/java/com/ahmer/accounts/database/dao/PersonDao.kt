package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.model.PersonsBalanceModel
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

    @Query(
        """SELECT p.*, 
            COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS balance
            FROM Persons p
            LEFT JOIN Transactions t ON p.id = t.personId
            WHERE p.name LIKE '%' || :searchQuery || '%'
            GROUP BY p.name ORDER BY balance DESC"""
    )
    fun getAllPersonsSortedByAmount(searchQuery: String): Flow<List<PersonsBalanceModel>>

    @Query(
        """SELECT p.*, 
            COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS balance
            FROM Persons p
            LEFT JOIN Transactions t ON p.id = t.personId
            WHERE p.name LIKE '%' || :searchQuery || '%'
            GROUP BY p.name ORDER BY created ASC"""
    )
    fun getAllPersonsSortedByDate(searchQuery: String): Flow<List<PersonsBalanceModel>>

    @Query(
        """SELECT p.*, 
            COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS balance
            FROM Persons p
            LEFT JOIN Transactions t ON p.id = t.personId
            WHERE p.name LIKE '%' || :searchQuery || '%'
            GROUP BY p.name ORDER BY name ASC"""
    )
    fun getAllPersonsSortedByName(searchQuery: String): Flow<List<PersonsBalanceModel>>

    fun getAllPersonsByFilter(
        searchQuery: String, sortOrder: SortOrder
    ): Flow<List<PersonsBalanceModel>> = when (sortOrder) {
        SortOrder.Amount -> getAllPersonsSortedByAmount(searchQuery = searchQuery)
        SortOrder.Date -> getAllPersonsSortedByDate(searchQuery = searchQuery)
        SortOrder.Name -> getAllPersonsSortedByName(searchQuery = searchQuery)
    }

    @Query("SELECT * FROM Persons WHERE id = :personId")
    fun getPersonById(personId: Int): Flow<PersonsEntity?>

    @Transaction
    @Query(
        """SELECT 
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum 
            FROM Transactions WHERE personId = :personId"""
    )
    fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel>

    @Transaction
    @Query(
        """SELECT
        SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
        SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum
        FROM Transactions"""
    )
    fun getAllAccountsBalance(): Flow<TransSumModel>
}