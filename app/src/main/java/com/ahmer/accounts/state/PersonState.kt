package com.ahmer.accounts.state

import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransSumModel

data class PersonState(
    val getAllPersonsList: ResultState<List<PersonsEntity>> = ResultState.Loading,
    val getAllPersonsBalance: TransSumModel = TransSumModel(),
)