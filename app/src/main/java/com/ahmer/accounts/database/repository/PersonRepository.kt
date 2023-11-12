package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.model.PersonsBalanceModel
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun insertOrUpdate(personsEntity: PersonsEntity)
    suspend fun delete(personsEntity: PersonsEntity)
    fun allPersons(): Flow<List<PersonsEntity>>
    fun allPersonsSearch(query: String, sortOrder: SortOrder): Flow<List<PersonsBalanceModel>>
    fun personById(personId: Int): Flow<PersonsEntity>
    fun balanceByPerson(personId: Int): Flow<TransSumModel>
    fun accountsBalance(): Flow<TransSumModel>
}