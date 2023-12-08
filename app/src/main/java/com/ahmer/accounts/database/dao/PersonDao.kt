package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.model.PersonsBalanceModel
import com.ahmer.accounts.database.model.TransSumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Upsert
    suspend fun insertOrUpdate(personsEntity: PersonsEntity)

    @Delete
    suspend fun delete(personsEntity: PersonsEntity)

    @Query("SELECT * FROM Persons")
    fun allPersons(): Flow<List<PersonsEntity>>

    @Query(
        """SELECT p.*, 
            COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS balance
            FROM Persons p
            LEFT JOIN Transactions t ON p.id = t.personId
            WHERE p.name LIKE '%' || :query || '%'
            GROUP BY p.name ORDER BY
            CASE :sort WHEN 0 THEN balance END ASC, 
            CASE :sort WHEN 1 THEN balance END DESC,
            CASE :sort WHEN 2 THEN p.created END ASC, 
            CASE :sort WHEN 3 THEN p.created END DESC,
            CASE :sort WHEN 4 THEN p.name END ASC, 
            CASE :sort WHEN 5 THEN p.name END DESC"""
    )
    fun allPersonsSearch(query: String, sort: Int): Flow<List<PersonsBalanceModel>>

    @Query("SELECT * FROM Persons WHERE id = :personId")
    fun personById(personId: Int): Flow<PersonsEntity>

    @Transaction
    @Query(
        """SELECT 
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum 
            FROM Transactions WHERE personId = :personId"""
    )
    fun balanceByPerson(personId: Int): Flow<TransSumModel>

    @Transaction
    @Query(
        """SELECT
        SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
        SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum
        FROM Transactions"""
    )
    fun accountsBalance(): Flow<TransSumModel>
}