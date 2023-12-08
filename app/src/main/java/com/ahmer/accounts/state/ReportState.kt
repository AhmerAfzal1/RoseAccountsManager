package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.TransEntity

data class ReportState(
    val allTransactions: List<TransEntity> = emptyList(),
)
