package com.ahmer.accounts.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
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

    @JvmStatic
    fun toastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}