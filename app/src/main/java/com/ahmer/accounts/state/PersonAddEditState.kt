package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.PersonEntity

data class PersonAddEditState(
    val person: PersonEntity? = null,
)