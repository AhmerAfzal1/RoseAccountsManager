package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.TransSumModel

data class MainState(
    val getAllPersonsBalance: TransSumModel = TransSumModel(),
)
