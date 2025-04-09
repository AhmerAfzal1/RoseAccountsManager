package com.ahmer.accounts.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a transaction summary containing credit and debit totals.
 *
 * @property creditSum The total sum of credit transactions. Should be non-negative.
 * @property debitSum The total sum of debit transactions. Should be non-negative.
 */
@Parcelize
data class TransactionSumModel(
    var creditSum: Double = 0.0,
    var debitSum: Double = 0.0
) : Parcelable {
    /**
     * Calculates the current balance by subtracting debit total from credit total.
     *
     * Note: Using Double for financial calculations might lead to precision errors in
     * some cases. Consider using [java.math.BigDecimal] for precise monetary calculations.
     */
    val balance: Double
        get() = creditSum - debitSum

    init {
        require(value = creditSum >= 0) { "Credit sum cannot be negative" }
        require(value = debitSum >= 0) { "Debit sum cannot be negative" }
    }
}