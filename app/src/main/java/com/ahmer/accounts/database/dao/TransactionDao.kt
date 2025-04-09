package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    /**
     * Inserts a new transaction or updates an existing one.
     *
     * @param transaction the transaction entity to upsert.
     */
    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity)

    /**
     * Deletes a transaction from the database.
     *
     * @param transaction the transaction entity to delete.
     */
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    /**
     * Retrieves all transactions for a specific person, sorted based on the provided sort type.
     *
     * Sort types:
     * 0 - Order by `created` ascending
     * 1 - Order by `date` ascending
     *
     * @param personId the ID of the person.
     * @param sort the sort type.
     * @return a Flow emitting the list of transactions.
     */
    @Query(
        """
        SELECT * FROM Transactions
        WHERE personId = :personId 
        ORDER BY 
            CASE :sort WHEN 0 THEN created END ASC,
            CASE :sort WHEN 1 THEN date END ASC
        """
    )
    fun getTransactionsByPersonId(personId: Int, sort: Int): Flow<List<TransactionEntity>>

    /**
     * Retrieves all transactions whose createdOn date is within the specified dates.
     *
     * @param dates the list of dates to filter transactions.
     * @return a Flow emitting the list of transactions.
     */
    @Query("SELECT * FROM Transactions WHERE createdOn IN(:dates)")
    fun getTransactionsBetweenDates(dates: List<String>): Flow<List<TransactionEntity>>

    /**
     * Retrieves all transactions for a specific date.
     *
     * @param date the date to filter transactions.
     * @return a Flow emitting the list of transactions.
     */
    @Query("SELECT * FROM Transactions WHERE createdOn = :date")
    fun getTransactionsByDate(date: String): Flow<List<TransactionEntity>>

    /**
     * Searches transactions for a given person by matching the search query against the date,
     * description, or amount fields. Results are ordered by the creation time.
     *
     * @param personId the ID of the person.
     * @param searchQuery the text to search in transactions.
     * @return a Flow emitting the list of matching transactions.
     */
    @Query(
        """
        SELECT * FROM Transactions 
        WHERE personId = :personId 
          AND (
              date LIKE '%' || :searchQuery || '%' OR 
              description LIKE '%' || :searchQuery || '%' OR 
              amount LIKE '%' || :searchQuery || '%'
          ) 
        ORDER BY created ASC
        """
    )
    fun searchTransactions(personId: Int, searchQuery: String): Flow<List<TransactionEntity>>

    /**
     * Retrieves all transactions.
     *
     * @return a Flow emitting the list of all transactions.
     */
    @Query("SELECT * FROM Transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction.
     * @return a Flow emitting the transaction entity.
     */
    @Query("SELECT * FROM Transactions WHERE id = :transactionId")
    fun getTransactionById(transactionId: Int): Flow<TransactionEntity>

    /**
     * Retrieves the balance summary for a specific person.
     *
     * The balance summary includes the total credits and debits.
     *
     * @param personId the ID of the person.
     * @return a Flow emitting the balance summary.
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
    fun getBalanceByPerson(personId: Int): Flow<TransactionSumModel>

    /**
     * Retrieves the overall balance summary for all transactions.
     *
     * The summary includes the total credits and debits.
     *
     * @return a Flow emitting the overall balance summary.
     */
    @Transaction
    @Query(
        value = """SELECT
            SUM(CASE WHEN type = 'Credit' THEN amount ELSE 0 END) AS creditSum,
            SUM(CASE WHEN type = 'Debit' THEN amount ELSE 0 END) AS debitSum
            FROM Transactions"""
    )
    fun getTotalBalance(): Flow<TransactionSumModel>
}