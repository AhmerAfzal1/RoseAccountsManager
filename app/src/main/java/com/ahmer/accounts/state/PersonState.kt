package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.PersonBalanceModel

data class PersonState(
    val allPersons: List<PersonBalanceModel> = emptyList(),
)