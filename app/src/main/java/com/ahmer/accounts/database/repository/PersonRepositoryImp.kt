package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.SortOrder
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class PersonRepositoryImp @Inject constructor(private val personDao: PersonDao) : PersonRepository {
    override suspend fun insertOrUpdate(personsEntity: PersonsEntity) {
        return withContext(Dispatchers.IO) {
            personDao.insertOrUpdate(personsEntity = personsEntity)
        }
    }

    override suspend fun delete(personsEntity: PersonsEntity) {
        return withContext(Dispatchers.IO) {
            personDao.delete(personsEntity = personsEntity)
        }
    }

    override fun getAllPersons(): Flow<List<PersonsEntity>> = personDao.getAllPersons()

    override fun getAllPersonsByFilter(
        searchQuery: String, sortOrder: SortOrder
    ): Flow<List<PersonsEntity>> {
        return personDao.getAllPersonsByFilter(searchQuery = searchQuery, sortOrder = sortOrder)
    }

    override fun getPersonById(personId: Int): Flow<PersonsEntity?> {
        return personDao.getPersonById(personId = personId)
    }

    override fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel> {
        return personDao.getAccountBalanceByPerson(personId = personId)
    }

    override fun getAllAccountsBalance(): Flow<TransSumModel> = personDao.getAllAccountsBalance()
}