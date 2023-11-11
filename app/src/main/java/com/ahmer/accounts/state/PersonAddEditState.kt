package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.PersonsEntity

data class PersonAddEditState(
    val getPersonDetails: PersonsEntity? = PersonsEntity(),
)