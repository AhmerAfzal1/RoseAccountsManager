package com.ahmer.accounts.utils

import android.Manifest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Constants {
    companion object {
        const val ADS_WAIT_TIME: Long = 250L
        const val ANIMATE_ITEM_DURATION: Int = 500
        const val DATA_STORE_KEY_CURRENCY: String = "Currency"
        const val DATA_STORE_KEY_SORT_BY: String = "SortBy"
        const val DATA_STORE_KEY_SORT_ORDER: String = "SortOrder"
        const val DATA_STORE_KEY_THEME: String = "Theme"
        const val DATA_STORE_NAME: String = "RoseDataStore"
        const val DATABASE_NAME: String = "RoseDatabase.db"
        const val DATABASE_PERSONS_TABLE: String = "Persons"
        const val DATABASE_TRANSACTION_TABLE: String = "Transactions"
        const val DATE_PATTERN: String = "dd MMM yyyy"
        const val DATE_SHORT_PATTERN: String = "dd MMM yy"
        const val DATE_TIME_FILE_NAME_PATTERN: String = "ddMMyyHHmmss"
        const val DATE_TIME_PATTERN: String = "dd MMM yyyy hh:mm:ss a"
        const val DATE_TIME_NEW_PATTERN: String = "dd MMM yyyy - h:mm a"
        const val DATE_TIME_PDF_PATTERN: String = "EEEE, dd MMMM yyyy - hh:mm:ss a"
        const val LOG_TAG: String = "RoseAccounts"
        const val PLAY_STORE_LINK: String = "https://play.google.com/store/apps/details?id="
        const val STATE_IN_STARTED_TIME: Long = 5000L
        val TOP_APP_BAR_HEIGHT: Dp = 64.dp
        val ICON_SIZE: Dp = 36.dp
        val PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }
}