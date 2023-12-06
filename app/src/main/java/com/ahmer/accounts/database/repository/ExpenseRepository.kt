package com.ahmer.accounts.database.repository

import com.ahmer.accounts.database.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun insertOrUpdate(expenseEntity: ExpenseEntity)
    suspend fun delete(expenseEntity: ExpenseEntity)
    fun allExpenses(): Flow<List<ExpenseEntity>>
}