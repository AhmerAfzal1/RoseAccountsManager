package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransSumModel(
    var creditSum: String? = "",
    var debitSum: String? = ""
) : Parcelable
