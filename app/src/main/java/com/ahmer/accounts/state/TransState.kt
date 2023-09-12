package com.ahmer.accounts.state

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel

data class TransState(
    val getAllPersonsTransList: ResultState<List<TransEntity>> = ResultState.Loading,
    val getPersonTransBalance: ResultState<TransSumModel> = ResultState.Loading
)