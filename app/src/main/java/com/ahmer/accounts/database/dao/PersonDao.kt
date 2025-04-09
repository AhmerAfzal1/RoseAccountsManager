package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.model.PersonBalanceModel
import com.ahmer.accounts.database.model.TransactionSumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    /**
     * Inserts a new person or updates an existing one.
     *
     * @param person the person entity to upsert.
     */
    @Upsert
    suspend fun upsertPerson(person: PersonEntity)

    /**
     * Deletes a person from the database.
     *
     * @param person the person entity to delete.
     */
    @Delete
    suspend fun deletePerson(person: PersonEntity)

    /**
     * Retrieves all persons as a Flow.
     *
     * @return a Flow emitting the list of all persons.
     */
    @Query("SELECT * FROM Persons")
    fun getAllPersons(): Flow<List<PersonEntity>>

    /**
     * Searches persons by name and sorts the results based on the provided sort type.
     *
     * Sort types:
     * 0 - Balance Ascending
     * 1 - Balance Descending
     * 2 - Created Date Ascending
     * 3 - Created Date Descending
     * 4 - Name Ascending
     * 5 - Name Descending
     *
     * @param query the search query for person names.
     * @param sort the sort type.
     * @return a Flow emitting the list of persons along with their balance.
     */
    @Query(
        """
        SELECT 
            p.*, 
            COALESCE(SUM(CASE WHEN t.type = 'Credit' THEN t.amount ELSE -t.amount END), 0) AS balance
        FROM Persons p
        LEFT JOIN Transactions t ON p.id = t.personId
        WHERE p.name LIKE '%' || :query || '%'
        GROUP BY p.name
        ORDER BY 
            CASE :sort WHEN 0 THEN balance END ASC, 
            CASE :sort WHEN 1 THEN balance END DESC,
            CASE :sort WHEN 2 THEN p.created END ASC, 
            CASE :sort WHEN 3 THEN p.created END DESC,
            CASE :sort WHEN 4 THEN p.name END ASC, 
            CASE :sort WHEN 5 THEN p.name END DESC
        """
    )
    fun searchPersons(query: String, sort: Int): Flow<List<PersonBalanceModel>>

    /**
     * Retrieves a person by their ID.
     *
     * @param personId the ID of the person.
     * @return a Flow emitting the person entity.
     */
    @Query("SELECT * FROM Persons WHERE id = :personId")
    fun getPersonById(personId: Int): Flow<PersonEntity>

    /**
     * Retrieves the balance summary for a given person.
     *
     * @param personId the ID of the person.
     * @return a Flow emitting the transaction summary model.
     */
    @Transaction
    @Query(
        """
        SELECT 
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum 
        FROM Transactions 
        WHERE personId = :personId
        """
    )
    fun balanceByPerson(personId: Int): Flow<TransactionSumModel>

    /**
     * Retrieves the overall accounts balance summary.
     *
     * @return a Flow emitting the overall transaction summary.
     */
    @Transaction
    @Query(
        """
        SELECT
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum
        FROM Transactions
        """
    )
    fun accountsBalance(): Flow<TransactionSumModel>
}