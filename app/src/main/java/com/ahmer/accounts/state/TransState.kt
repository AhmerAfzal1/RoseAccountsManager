package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel

data class TransState(
    val getAllPersonsTransList: List<TransEntity> = emptyList(),
    val getPersonTransBalance: TransSumModel = TransSumModel(),
)