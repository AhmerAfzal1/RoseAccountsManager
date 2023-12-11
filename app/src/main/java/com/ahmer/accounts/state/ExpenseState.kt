package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.ExpenseEntity

data class ExpenseState(
    val allExpenses: List<ExpenseEntity> = emptyList(),
)
