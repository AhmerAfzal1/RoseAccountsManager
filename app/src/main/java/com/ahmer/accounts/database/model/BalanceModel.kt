package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the balance information for an account.
 *
 * @property balance The current balance amount. Defaults to 0.0.
 */
@Parcelize
data class BalanceModel(
    val balance: Double = 0.0
) : Parcelable