package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.PersonsBalanceModel

data class PersonState(
    val getAllPersonsList: List<PersonsBalanceModel> = emptyList(),
)