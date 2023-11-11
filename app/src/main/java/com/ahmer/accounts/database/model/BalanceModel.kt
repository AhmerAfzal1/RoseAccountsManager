package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceModel(
    var balance: Double = 0.0,
) : Parcelable