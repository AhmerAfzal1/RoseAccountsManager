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

object HelperFunctions {

    @JvmStatic
    fun getDateTime(time: Long): String = if (time == 0.toLong()) {
        ""
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN))
        } else {
            SimpleDateFormat(Constants.DATE_TIME_PATTERN, Locale.getDefault()).format(Date(time))
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
        var result = size.toDouble() / 1024
        if (result < 1024) return "${result.roundToInt()} KB"
        result /= 1024
        if (result < 1024) return String.format("%.2f MB", result)
        result /= 1024
        return String.format("%.2f GB", result)

    }

    @JvmStatic
    fun toastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}