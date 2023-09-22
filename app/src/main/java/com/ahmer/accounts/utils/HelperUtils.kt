package com.ahmer.accounts.utils

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object HelperUtils {

    private fun getContentFileName(context: Context, uri: Uri): String? = runCatching {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                .let(cursor::getString)
        }
    }.getOrNull()

    @JvmStatic
    fun getFileNameFromDatabase(context: Context, uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(context, uri)
        else -> uri.path?.let(::File)?.name
    }

    @JvmStatic
    fun getDateTime(time: Long, pattern: String = ""): String = if (time == 0.toLong()) {
        ""
    } else {
        val mPattern = pattern.ifEmpty {
            Constants.DATE_TIME_PATTERN
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(mPattern))
        } else {
            SimpleDateFormat(mPattern, Locale.getDefault()).format(Date(time))
        }
    }

    fun getPlayStoreLink(context: Context): String = Constants.PLAY_STORE_LINK + context.packageName

    @JvmStatic
    fun getRoundedValue(value: Double): String {
        val mRound = DecimalFormat("#,##0.##")
        mRound.roundingMode = RoundingMode.HALF_UP
        return mRound.format(value)
    }

    @JvmStatic
    fun getSizeFormat(size: Long): String {
        var mResult = size.toDouble() / 1024
        if (mResult < 1024) return "${mResult.roundToInt()} KB"
        mResult /= 1024
        if (mResult < 1024) return String.format("%.2f MB", mResult)
        mResult /= 1024
        return String.format("%.2f GB", mResult)

    }

    fun isGrantedPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

    }

    fun isGrantedPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    @JvmStatic
    fun toastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun relaunchApp(context: Context) {
        val mPackageManager: PackageManager = context.packageManager
        val mIntent: Intent = mPackageManager.getLaunchIntentForPackage(context.packageName)!!
        val mComponentName: ComponentName = mIntent.component!!
        val mRestartIntent: Intent = Intent.makeRestartActivityTask(mComponentName)
        context.startActivity(mRestartIntent)
        Runtime.getRuntime().exit(0)
    }
}