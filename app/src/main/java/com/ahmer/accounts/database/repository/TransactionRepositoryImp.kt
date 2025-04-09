package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.TransactionDao
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [TransactionRepository] for managing transaction-related database operations.
 */
@ViewModelScoped
class TransactionRepositoryImp @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    /**
     * Inserts or updates a transaction entity in the database.
     *
     * @param transaction The transaction entity to be inserted or updated.
     */
    override suspend fun insertOrUpdate(transaction: TransactionEntity) {
        withContext(context = Dispatchers.IO) {
            transactionDao.upsertTransaction(transaction = transaction)
        }
    }

    /**
     * Deletes a transaction entity from the database.
     *
     * @param transaction The transaction entity to be deleted.
     */
    override suspend fun delete(transaction: TransactionEntity) {
        withContext(context = Dispatchers.IO) {
            transactionDao.deleteTransaction(transaction = transaction)
        }
    }

    /**
     * Retrieves all transactions associated with a specific person, sorted by a given criterion.
     *
     * @param personId The ID of the person whose transactions are being retrieved.
     * @param sort The sorting criterion: `0` for creation date, `1` for transaction date.
     * @return A [Flow] emitting a list of transactions for the specified person.
     */
    override fun getTransactionsByPersonId(
        personId: Int, sort: Int
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByPersonId(personId = personId, sort = sort)
    }

    /**
     * Retrieves all transactions that occurred between the specified dates.
     *
     * @param dateRange A list containing the start and end dates for filtering transactions.
     * @return A [Flow] emitting a list of transactions within the specified date range.
     */
    override fun getTransactionsBetweenDates(dateRange: List<String>): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsBetweenDates(dates = dateRange)
    }

    /**
     * Retrieves all transactions that occurred on a specific date.
     *
     * @param date The date for filtering transactions.
     * @return A [Flow] emitting a list of transactions for the given date.
     */
    override fun getTransactionsByDate(date: String): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByDate(date = date)
    }

    /**
     * Searches for transactions related to a specific person based on a search query.
     *
     * @param personId The ID of the person whose transactions are being searched.
     * @param searchQuery The search term for filtering transactions.
     * @return A [Flow] emitting a list of transactions that match the search query.
     */
    override fun searchTransactions(
        personId: Int, searchQuery: String
    ): Flow<List<TransactionEntity>> {
        return transactionDao.searchTransactions(personId = personId, searchQuery = searchQuery)
    }

    /**
     * Retrieves all transactions from the database.
     *
     * @return A [Flow] emitting a list of all transactions.
     */
    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    /**
     * Retrieves a transaction entity by its unique identifier.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @return A [Flow] emitting the requested transaction entity or null if not found.
     */
    override fun getTransactionById(transactionId: Int): Flow<TransactionEntity?> {
        return transactionDao.getTransactionById(transactionId = transactionId)
    }

    /**
     * Retrieves the financial balance summary for a specific person.
     *
     * @param personId The ID of the person whose balance is being retrieved.
     * @return A [Flow] emitting the transaction summary for the specified person.
     */
    override fun getBalanceByPerson(personId: Int): Flow<TransactionSumModel> {
        return transactionDao.getBalanceByPerson(personId = personId)
    }

    /**
     * Retrieves the aggregated financial balance across all accounts.
     *
     * @return A [Flow] emitting the total transaction summary.
     */
    override fun getBalanceForAllAccounts(): Flow<TransactionSumModel> =
        transactionDao.getTotalBalance()
}