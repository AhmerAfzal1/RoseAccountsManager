package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.TransEntity

data class TransAddEditState(
    val getTransDetails: TransEntity? = TransEntity(),
)