package com.ahmer.accounts.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Constants {
    companion object {
        const val ADS_WAIT_TIME: Long = 250L
        const val DATABASE_NAME: String = "RoseDatabase.db"
        const val DATABASE_PERSONS_TABLE: String = "Persons"
        const val DATABASE_TRANSACTION_TABLE: String = "Transactions"
        const val DATE_PATTERN: String = "dd MMM yyyy"
        const val DATE_SHORT_PATTERN: String = "dd-MMM-yy"
        const val DATE_TIME_PATTERN: String = "dd MMM yyyy hh:mm:ss a"
        const val LOG_TAG: String = "RoseAccounts"
        const val PLAY_STORE_LINK: String = "https://play.google.com/store/apps/details?id="
        const val PREFERENCES_NAME: String = "RoseAccountsPrefs"
        val ICON_SIZE: Dp = 30.dp
    }
}