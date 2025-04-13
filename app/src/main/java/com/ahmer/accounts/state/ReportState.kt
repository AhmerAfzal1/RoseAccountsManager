package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel

data class ReportState(
    val transactions: List<TransactionEntity> = emptyList(),
    val transactionSum: TransactionSumModel = TransactionSumModel(),
)
