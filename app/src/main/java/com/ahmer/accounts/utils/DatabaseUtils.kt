package com.ahmer.accounts.utils

import android.content.Intent

object DatabaseUtils {

    @JvmStatic
    fun backupDatabase(): Intent {
        val mFileName =
            "backup_${
                HelperUtils.getDateTime(
                    time = System.currentTimeMillis(),
                    pattern = Constants.DATE_TIME_FILE_NAME_PATTERN
                )
            }.db"
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            val mMimeType = "application/octet-stream"
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(mMimeType))
            putExtra(Intent.EXTRA_TITLE, mFileName)
            type = mMimeType
        }
    }

    @JvmStatic
    fun restoreDatabaseBackup(): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            type = "*/*"
            Intent.createChooser(this, "Select the database backup file")
        }
    }
}