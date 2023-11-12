package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel

data class TransState(
    val allTransactions: List<TransEntity> = emptyList(),
    val transSumModel: TransSumModel = TransSumModel(),
)