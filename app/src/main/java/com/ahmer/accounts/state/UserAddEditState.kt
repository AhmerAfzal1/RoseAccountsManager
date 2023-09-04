package com.ahmer.accounts.state

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.UserModel

data class UserAddEditState(
    val getUserDetails: ResultState<UserModel?> = ResultState.Loading,
)