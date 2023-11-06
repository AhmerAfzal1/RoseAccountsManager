package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonsBalanceModel(
    @Embedded
    val personsEntity: PersonsEntity,
    @Embedded
    val balanceModel: BalanceModel
) : Parcelable