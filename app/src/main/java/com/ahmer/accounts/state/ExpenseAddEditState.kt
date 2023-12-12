package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.ExpenseEntity

data class ExpenseAddEditState(
    val expense: ExpenseEntity? = ExpenseEntity(),
)