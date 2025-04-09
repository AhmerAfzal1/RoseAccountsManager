package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.TransactionEntity

data class ReportState(
    val allTransactions: List<TransactionEntity> = emptyList(),
)
