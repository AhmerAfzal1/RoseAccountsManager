package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel

data class TransState(
    val allTransactions: List<TransactionEntity> = emptyList(),
    val transactionSumModel: TransactionSumModel = TransactionSumModel(),
)