package com.ahmer.accounts.state

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.model.TransSumModel

data class TransState(
    val getAllUsersTransList: ResultState<List<TransModel>> = ResultState.Loading,
    val getUserTransBalance: ResultState<TransSumModel> = ResultState.Loading
)