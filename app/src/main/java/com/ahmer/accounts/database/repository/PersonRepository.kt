package com.ahmer.accounts.database.repository

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    suspend fun insertOrUpdate(personsEntity: PersonsEntity)

    suspend fun delete(personsEntity: PersonsEntity)

    fun getAllPersons(): Flow<List<PersonsEntity>>

    fun getAllPersonsByFilter(
        searchQuery: String, sortOrder: SortOrder
    ): Flow<ResultState<List<PersonsEntity>>>

    fun getPersonById(personId: Int): Flow<ResultState<PersonsEntity?>>

    fun getAccountBalanceByPerson(personId: Int): Flow<TransSumModel>

    fun getAllAccountsBalance(): Flow<TransSumModel>
}