package com.ahmer.accounts.utils

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.ahmer.accounts.R
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File
import java.math.BigDecimal
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
    @Composable
    fun AdjustableText(
        modifier: Modifier = Modifier,
        text: String,
        color: Color,
        length: Int = 0,
        isBold: Boolean = true
    ) {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontWeight = if (isBold && length <= 17) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            style = when (if (length == 0) text.length else length) {
                in 0..10 -> MaterialTheme.typography.bodyMedium
                in 11..13 -> MaterialTheme.typography.bodySmall
                else -> MaterialTheme.typography.labelSmall
            }
        )
    }

    @Composable
    fun AmountWithSymbolText(
        modifier: Modifier = Modifier,
        modifierSymbol: Modifier = Modifier,
        modifierAmount: Modifier = Modifier,
        currency: Currency,
        amount: Double,
        color: Color,
        isBold: Boolean = true
    ) {
        Row(
            modifier = modifier, verticalAlignment = Alignment.CenterVertically
        ) {
            val textLength: Int =
                currency.symbol.length + getRoundedValue(value = amount).length - 1
            /*Log.v(
                Constants.LOG_TAG, "TextLen: Currency -> ${currency.symbol.length}, " +
                        "Amount -> $amount: ${getRoundedValue(value = amount).length}, " +
                        "TotalLen -> $textLength"
            )*/
            AdjustableText(
                text = "${currency.symbol} ",
                modifier = modifierSymbol,
                color = color,
                length = textLength,
                isBold = isBold,
            )
            AdjustableText(
                text = getRoundedValue(value = amount),
                modifier = modifierAmount,
                color = color,
                length = textLength,
                isBold = isBold,
            )
        }
    }

    fun getAppInfo(context: Context): AppVersion {
        val mAppVersion: AppVersion by lazy {
            val mVersion = AppVersion()
            context.packageManager.getPackageInfo(
                context.packageName, PackageManager.GET_ACTIVITIES
            )?.let { packageInfo ->
                mVersion.versionName = packageInfo.versionName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mVersion.versionCode = packageInfo.longVersionCode
                } else {
                    @Suppress("DEPRECATION")
                    mVersion.versionCode = packageInfo.versionCode.toLong()
                }
            }
            mVersion
        }
        return mAppVersion
    }

    private fun getContentFileName(context: Context, uri: Uri): String? = runCatching {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                .let(cursor::getString)
        }
    }.getOrNull()

    fun getFileNameFromDatabase(context: Context, uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(context = context, uri = uri)
        else -> uri.path?.let(::File)?.name
    }

    fun getDateTime(time: Long, pattern: String = ""): String = if (time == 0.toLong()) {
        ""
    } else {
        val mPattern = pattern.ifEmpty { Constants.PATTERN_GENERAL }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(mPattern))
        } else {
            SimpleDateFormat(mPattern, Locale.getDefault()).format(Date(time))
        }
    }

    fun getDirSize(directory: File): Long {
        var mSize = 0.toLong()
        directory.listFiles()?.forEach { file ->
            if (file != null && file.isDirectory) {
                mSize += getDirSize(directory = file)
            } else if (file != null && file.isFile) {
                mSize += file.length()
            }
        }
        return mSize
    }

    fun getCacheSize(context: Context): String {
        var mSize = 0.toLong()
        val mExternalCacheDir = context.externalCacheDir
        mSize += getDirSize(directory = context.cacheDir)
        if (mExternalCacheDir != null) {
            mSize += getDirSize(directory = mExternalCacheDir)
        }
        mSize += getDirSize(directory = context.codeCacheDir)
        return getSizeFormat(size = mSize)
    }

    fun getPlayStoreLink(context: Context): String = Constants.PLAY_STORE_LINK + context.packageName

    fun getRoundedValue(value: Double): String {
        val mFormat = DecimalFormat("#,##0.##")
        val mRound: BigDecimal = BigDecimal(value).setScale(2, RoundingMode.HALF_UP)
        return mFormat.format(mRound)
    }

    fun getSizeFormat(size: Long): String {
        var mResult = size.toDouble() / 1024
        if (mResult < 1024) return "${mResult.roundToInt()} KB"
        mResult /= 1024
        if (mResult < 1024) return String.format(format = "%.2f MB", mResult)
        mResult /= 1024
        return String.format(format = "%.2f GB", mResult)
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

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun relaunchApp(context: Context) {
        val mPackageManager: PackageManager = context.packageManager
        val mIntent: Intent = mPackageManager.getLaunchIntentForPackage(context.packageName)!!
        val mComponentName: ComponentName = mIntent.component!!
        val mRestartIntent: Intent = Intent.makeRestartActivityTask(mComponentName)
        context.startActivity(mRestartIntent)
        Runtime.getRuntime().exit(0)
    }

    fun moreApps(context: Context, developerId: String = "Ahmer Afzal") {
        try {
            ContextCompat.startActivity(
                context, Intent(
                    Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:$developerId&hl=en")
                ), null
            )
        } catch (e: ActivityNotFoundException) {
            ContextCompat.startActivity(
                context, Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=$developerId&hl=en")
                ), null
            )
        }
    }

    fun runWeb(context: Context, packageName: String) {
        try {
            ContextCompat.startActivity(
                context,
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")),
                null
            )
        } catch (e: ActivityNotFoundException) {
            ContextCompat.startActivity(
                context, Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                ), null
            )
        }
    }

    fun shareApp(context: Context) {
        try {
            val mIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=${context.packageName}"
                )
            }
            ContextCompat.startActivity(context, Intent.createChooser(mIntent, "Choose one"), null)
        } catch (e: Exception) {
            Log.e(Constants.LOG_TAG, "Exception while share app: ${e.localizedMessage}", e)
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}