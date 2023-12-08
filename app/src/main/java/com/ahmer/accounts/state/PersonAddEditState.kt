package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.PersonsEntity

data class PersonAddEditState(
    val person: PersonsEntity? = PersonsEntity(),
)