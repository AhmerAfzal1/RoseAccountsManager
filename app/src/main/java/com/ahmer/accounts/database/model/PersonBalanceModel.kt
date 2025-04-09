package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.room.Embedded
import com.ahmer.accounts.database.entity.PersonEntity
import kotlinx.parcelize.Parcelize

/**
 * Represents the combined data of a person and their balance.
 *
 * @property personsEntity The person's entity containing personal information.
 * @property balanceModel The balance model associated with the person.
 */
@Parcelize
data class PersonBalanceModel(
    @Embedded val personsEntity: PersonEntity,
    @Embedded val balanceModel: BalanceModel
) : Parcelable