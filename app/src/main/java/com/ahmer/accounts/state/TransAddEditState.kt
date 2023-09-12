package com.ahmer.accounts.state

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransEntity

data class TransAddEditState(
    val getTransDetails: ResultState<TransEntity?> = ResultState.Loading,
)