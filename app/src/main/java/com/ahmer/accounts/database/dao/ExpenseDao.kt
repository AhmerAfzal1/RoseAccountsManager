package com.ahmer.accounts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ahmer.accounts.database.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun insertOrUpdate(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun delete(expenseEntity: ExpenseEntity)

    @Query(value = "SELECT * FROM Expenses")
    fun allExpenses(): Flow<List<ExpenseEntity>>

    @Query(value = "SELECT * FROM Expenses WHERE id = :expenseId")
    fun expenseById(expenseId: Int): Flow<ExpenseEntity>
}