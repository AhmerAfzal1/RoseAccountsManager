package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceModel(
    var balance: String? = "0.0",
) : Parcelable