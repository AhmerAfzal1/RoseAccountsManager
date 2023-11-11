package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransSumModel(
    var creditSum: Double = 0.0,
    var debitSum: Double = 0.0,
) : Parcelable {
    val balance: Double
        get() = creditSum.minus(debitSum)
}