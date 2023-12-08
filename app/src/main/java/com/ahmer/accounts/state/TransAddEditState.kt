package com.ahmer.accounts.state

import com.ahmer.accounts.database.entity.TransEntity

data class TransAddEditState(
    val transaction: TransEntity? = TransEntity(),
)