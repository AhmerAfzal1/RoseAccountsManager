package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing transaction entities and their associated financial data.
 */
interface TransactionRepository {

    /**
     * Inserts a new transaction entity or updates an existing one in the database.
     *
     * @param transaction The transaction entity to be inserted or updated.
     */
    suspend fun insertOrUpdate(transaction: TransactionEntity)

    /**
     * Deletes a transaction entity from the database.
     *
     * @param transaction The transaction entity to be deleted.
     */
    suspend fun delete(transaction: TransactionEntity)

    /**
     * Retrieves all transaction entities associated with a specific person, sorted by the specified criterion.
     *
     * @param personId The ID of the person whose transactions are to be retrieved.
     * @param sort The sorting criterion: `0` for creation date, `1` for transaction date.
     * @return A Flow emitting a list of transactions for the specified person, sorted accordingly.
     */
    fun getTransactionsByPersonId(personId: Int, sort: Int): Flow<List<TransactionEntity>>

    /**
     * Retrieves all transaction entities that occurred between the specified dates.
     *
     * @param dateRange A list containing the start and end dates for the query.
     * @return A Flow emitting a list of transactions within the specified date range.
     */
    fun getTransactionsBetweenDates(dateRange: List<String>): Flow<List<TransactionEntity>>

    /**
     * Retrieves all transaction entities that occurred on a specific date.
     *
     * @param date The date for which transactions are to be retrieved.
     * @return A Flow emitting a list of transactions on the specified date.
     */
    fun getTransactionsByDate(date: String): Flow<List<TransactionEntity>>

    /**
     * Searches for transaction entities associated with a specific person that match the given search query.
     *
     * @param personId The ID of the person whose transactions are to be searched.
     * @param searchQuery The query string used to filter transactions.
     * @return A Flow emitting a list of transactions matching the search criteria.
     */
    fun searchTransactions(personId: Int, searchQuery: String): Flow<List<TransactionEntity>>

    /**
     * Retrieves all transaction entities from the database.
     *
     * @return A Flow emitting a list of all transactions.
     */
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    /**
     * Retrieves a specific transaction entity by its unique identifier.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return A Flow emitting the requested transaction entity, or null if not found.
     */
    fun getTransactionById(transactionId: Int): Flow<TransactionEntity?>

    /**
     * Retrieves the financial balance summary for a specific person.
     *
     * @param personId The ID of the person whose balance is to be retrieved.
     * @return A Flow emitting the transaction summary for the specified person.
     */
    fun getBalanceByPerson(personId: Int): Flow<TransactionSumModel>

    /**
     * Retrieves the aggregated financial balance across all accounts.
     *
     * @return A Flow emitting the total transaction summary.
     */
    fun getBalanceForAllAccounts(): Flow<TransactionSumModel>
}