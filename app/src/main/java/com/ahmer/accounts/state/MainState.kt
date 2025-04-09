package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.TransactionSumModel

data class MainState(
    val accountsBalance: TransactionSumModel = TransactionSumModel(),
)
