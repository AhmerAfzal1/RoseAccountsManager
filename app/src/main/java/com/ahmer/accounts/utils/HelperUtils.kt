package com.ahmer.accounts.utils

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.ahmer.accounts.R
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

object HelperUtils {
    private val formatterCache = ConcurrentHashMap<String, DateTimeFormatter>()
    private val decimalFormat = DecimalFormat("#,##0.##").apply {
        roundingMode = RoundingMode.HALF_UP
    }

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
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
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
        context: Context,
        currency: Currency,
        amount: Double,
        color: Color? = null,
        isBold: Boolean = true,
        isExpense: Boolean = false,
        type: String? = null,
    ) {
        Row(
            modifier = modifier, verticalAlignment = Alignment.CenterVertically
        ) {
            val mTextLength: Int =
                currency.symbol.length + roundValue(value = amount).length - 1
            val mValue: String = roundValue(value = amount)
            val mAmount: String = if (isExpense) {
                if (type == Constants.TYPE_INCOME) "+$mValue" else "-$mValue"
            } else mValue

            val mColor: Color = color
                ?: if (type == Constants.TYPE_EXPENSE || type == Constants.TYPE_DEBIT) {
                    colorRedDark
                } else colorGreenDark

            AdjustableText(
                text = "${currency.symbol} ",
                modifier = modifierSymbol,
                color = mColor,
                length = mTextLength,
                isBold = isBold,
            )
            AdjustableText(
                text = mAmount,
                modifier = modifierAmount,
                color = mColor,
                length = mTextLength,
                isBold = isBold,
            )
        }
    }

    @Composable
    fun ListDivider(thickness: Dp, alpha: Float) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = thickness,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
        )
    }

    fun appInfo(context: Context): AppVersion {
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

    private fun contentFileName(context: Context, uri: Uri): String? = runCatching {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))

        }
    }.getOrNull()

    fun fileNameFromDatabase(context: Context, uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> contentFileName(context = context, uri = uri)
        else -> uri.path?.let(::File)?.name
    }

    fun getDateTime(time: Long, pattern: String = Constants.PATTERN_GENERAL): String {
        if (time == 0L) return ""
        return formatterCache.getOrPut(pattern) {
            DateTimeFormatter.ofPattern(pattern)
        }.format(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
        )
    }

    fun dirSize(directory: File): Long {
        return directory.walk()
            .filter { it.isFile }
            .fold(initial = 0L) { acc, file -> acc + file.length() }
    }

    fun cacheSize(context: Context): String {
        val mCacheDirs = listOfNotNull(
            context.cacheDir,
            context.externalCacheDir,
            context.codeCacheDir
        )
        return sizeFormat(size = mCacheDirs.sumOf { dirSize(directory = it) })
    }

    fun playStoreLink(context: Context): String =
        "${Constants.PLAY_STORE_LINK}${context.packageName}"

    fun roundValue(value: Double): String {
        return try {
            decimalFormat.format(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP))
        } catch (e: Exception) {
            when (e) {
                is NumberFormatException -> "0.00"
                else -> throw e
            }
        }
    }

    fun sizeFormat(size: Long): String {
        var mResult = size.toDouble() / 1024
        if (mResult < 1024) return "${mResult.roundToInt()} KB"
        mResult /= 1024
        if (mResult < 1024) return String.format(
            locale = Locale.getDefault(), format = "%.2f MB", mResult
        )
        mResult /= 1024
        return String.format(locale = Locale.getDefault(), format = "%.2f GB", mResult)
    }

    fun isGrantedPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun isGrantedPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all { isGrantedPermission(context = context, permission = it) }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun relaunchApp(context: Context) {
        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let {
            with(Intent.makeRestartActivityTask(it.component)) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(this)
            }
        }
    }

    fun moreApps(context: Context, developerId: String = "Ahmer Afzal") {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://search?q=pub:$developerId".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/developer?id=$developerId".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

    fun runWeb(context: Context, packageName: String) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }
    }

    fun shareApp(context: Context) {
        try {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
                putExtra(Intent.EXTRA_TEXT, playStoreLink(context))
            }.let { Intent.createChooser(it, context.getString(R.string.label_share_app)) }.run {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("Share failed: ${e.localizedMessage}")
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}