package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.dao.ExpenseDao
import com.ahmer.accounts.database.entity.ExpenseEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class ExpenseRepositoryImp @Inject constructor(private val expenseDao: ExpenseDao) :
    ExpenseRepository {
    override suspend fun insertOrUpdate(expenseEntity: ExpenseEntity) {
        return withContext(Dispatchers.IO) {
            expenseDao.insertOrUpdate(expenseEntity = expenseEntity)
        }
    }

    override suspend fun delete(expenseEntity: ExpenseEntity) {
        return withContext(Dispatchers.IO) {
            expenseDao.delete(expenseEntity = expenseEntity)
        }
    }

    override fun allExpenses(): Flow<List<ExpenseEntity>> = expenseDao.allExpenses()

    override fun expenseById(expenseId: Int): Flow<ExpenseEntity> {
        return expenseDao.expenseById(expenseId = expenseId)
    }

}