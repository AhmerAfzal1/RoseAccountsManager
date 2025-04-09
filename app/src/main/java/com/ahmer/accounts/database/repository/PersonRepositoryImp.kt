package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.model.PersonBalanceModel
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.SortOrder
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class PersonRepositoryImp @Inject constructor(
    private val personDao: PersonDao
) : PersonRepository {

    /**
     * Inserts or updates a person entity in the database.
     *
     * @param person The person entity to be inserted or updated.
     */
    override suspend fun insertOrUpdate(person: PersonEntity) {
        withContext(context = Dispatchers.IO) {
            personDao.upsertPerson(person = person)
        }
    }

    /**
     * Deletes a person entity from the database.
     *
     * @param person The person entity to be deleted.
     */
    override suspend fun delete(person: PersonEntity) {
        withContext(context = Dispatchers.IO) {
            personDao.deletePerson(person = person)
        }
    }

    /**
     * Retrieves all persons from the database.
     *
     * @return A [Flow] emitting a list of all persons.
     */
    override fun getAllPersons(): Flow<List<PersonEntity>> = personDao.getAllPersons()

    /**
     * Searches for persons based on a query string and a specified sorting order.
     *
     * @param query The search term used to filter persons by their attributes.
     * @param sortOrder The sorting criteria determining the attribute and order (ascending or descending) to sort the results by. It can be one of the following:
     * - [SortOrder.Amount]: Sorts by the amount associated with each person.
     * - [SortOrder.Date]: Sorts by the date associated with each person.
     * - [SortOrder.Name]: Sorts by the name of each person.
     *
     * Each of these can be further specified with a [SortBy] to determine the direction:
     * - [SortBy.Ascending]: Sorts the results in ascending order.
     * - [SortBy.Descending]: Sorts the results in descending order.
     *
     * For example, to sort by amount in ascending order, you would pass `SortOrder.Amount(SortBy.Ascending)`.
     *
     * @return A [Flow] emitting a list of [PersonBalanceModel] objects that match the search query and are ordered according to the specified [sortOrder].
     */
    override fun searchPersons(
        query: String, sortOrder: SortOrder
    ): Flow<List<PersonBalanceModel>> {
        val sortType = when (sortOrder) {
            is SortOrder.Amount -> if (sortOrder.sortBy is SortBy.Ascending) 0 else 1
            is SortOrder.Date -> if (sortOrder.sortBy is SortBy.Ascending) 2 else 3
            is SortOrder.Name -> if (sortOrder.sortBy is SortBy.Ascending) 4 else 5
        }
        return personDao.searchPersons(query = query, sort = sortType)
    }

    /**
     * Retrieves a person entity by their unique identifier.
     *
     * @param personId The ID of the person to retrieve.
     * @return A [Flow] emitting the requested person entity.
     */
    override fun getPersonById(personId: Int): Flow<PersonEntity> {
        return personDao.getPersonById(personId = personId)
    }

    /**
     * Retrieves the financial balance summary for a specific person.
     *
     * @param personId The ID of the person whose balance is being retrieved.
     * @return A [Flow] emitting the transaction summary for the specified person.
     */
    override fun getBalanceByPerson(personId: Int): Flow<TransactionSumModel> {
        return personDao.balanceByPerson(personId = personId)
    }

    /**
     * Retrieves the aggregated financial balance across all accounts.
     *
     * @return A [Flow] emitting the total transaction summary.
     */
    override fun getAccountsBalance(): Flow<TransactionSumModel> = personDao.accountsBalance()
}