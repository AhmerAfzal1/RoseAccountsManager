package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.core.ResultState.Companion.flowMap
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.SortBy
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class PersonRepositoryImp @Inject constructor(private val personDao: PersonDao) : PersonRepository {

    override suspend fun insertOrUpdate(personsEntity: PersonsEntity) =
        withContext(Dispatchers.IO) {
            personDao.insertOrUpdate(personsEntity)
        }

    override suspend fun delete(personsEntity: PersonsEntity) =
        withContext(Dispatchers.IO) {
            personDao.delete(personsEntity)
        }

    override fun getAllPersons(): Flow<List<PersonsEntity>> = personDao.getAllPersons()

    override fun getAllPersonsByFilter(
        searchQuery: String, sortBy: SortBy
    ): Flow<ResultState<List<PersonsEntity>>> {
        return flowMap {
            personDao.getAllPersonsByFilter(searchQuery, sortBy).map { personsList ->
                ResultState.Success(personsList)
            }
        }
    }

    override fun getPersonById(personId: Int): Flow<ResultState<PersonsEntity?>> {
        return flowMap {
            personDao.getPersonById(personId).map { person ->
                ResultState.Success(person)
            }
        }
    }

    override fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel> {
        return personDao.getAccountBalanceByPerson(personId)
    }

    override fun getAllAccountsBalance(): Flow<TransSumModel> = personDao.getAllAccountsBalance()
}