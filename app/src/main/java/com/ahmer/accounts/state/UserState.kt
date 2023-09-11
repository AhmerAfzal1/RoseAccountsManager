package com.ahmer.accounts.state

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.database.model.UserModel

data class UserState(
    val getAllUsersList: ResultState<List<UserModel>> = ResultState.Loading,
    val getUserBalance: TransSumModel = TransSumModel(),
    val getAllUsersBalance: TransSumModel = TransSumModel(),
)