package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.TransactionEntity

data class TransAddEditState(
    val transaction: TransactionEntity? = TransactionEntity(),
)