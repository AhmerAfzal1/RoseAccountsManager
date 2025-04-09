package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.model.PersonBalanceModel
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction layer for managing person entities and their associated financial data.
 */
interface PersonRepository {
    /**
     * Creates or updates a person entity in the database.
     * @param person The person entity to be inserted or updated
     */
    suspend fun insertOrUpdate(person: PersonEntity)

    /**
     * Removes a person entity from the database.
     * @param person The person entity to be deleted
     */
    suspend fun delete(person: PersonEntity)

    /**
     * Get all person entities in the database.
     * @return Flow emitting list of all persons
     */
    fun getAllPersons(): Flow<List<PersonEntity>>

    /**
     * Get filtered and sorted person records with their balance information.
     * @param query Search term for filtering persons
     * @param sortOrder Sorting criteria for the results
     * @return Flow emitting list of matching persons with balances
     */
    fun searchPersons(query: String, sortOrder: SortOrder): Flow<List<PersonBalanceModel>>

    /**
     * Get a specific person entity by its unique identifier.
     * @param personId The ID of the person to retrieve
     * @return Flow emitting the requested person entity
     */
    fun getPersonById(personId: Int): Flow<PersonEntity>

    /**
     * Get the financial balance summary for a specific person.
     * @param personId The ID of the person to get balance for
     * @return Flow emitting transaction summary for the person
     */
    fun getBalanceByPerson(personId: Int): Flow<TransactionSumModel>

    /**
     * Get the aggregated financial balance across all accounts.
     * @return Flow emitting the total transaction summary
     */
    fun getAccountsBalance(): Flow<TransactionSumModel>
}