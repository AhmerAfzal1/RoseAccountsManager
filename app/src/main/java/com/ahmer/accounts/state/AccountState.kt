package com.ahmer.accounts.state

import com.ahmer.accounts.database.model.PersonBalanceModel

data class AccountState(
    val allAccounts: List<PersonBalanceModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)