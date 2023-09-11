package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransSumModel(
    var creditSum: String? = "0.0",
    var debitSum: String? = "0.0"
) : Parcelable
