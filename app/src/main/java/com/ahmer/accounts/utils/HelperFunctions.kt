package com.ahmer.accounts.utils

import android.content.Context
import android.widget.Toast
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object HelperFunctions {

    @JvmStatic
    fun getDateTime(time: Long): String = if (time == 0.toLong()) "" else {
        SimpleDateFormat(Constants.DATE_TIME_PATTERN, Locale.getDefault()).format(Date(time))
    }

    fun getPlayStoreLink(context: Context): String = Constants.PLAY_STORE_LINK + context.packageName

    @JvmStatic
    fun getRoundedValue(value: Double): String {
        val mRound = DecimalFormat("#,##0.##")
        mRound.roundingMode = RoundingMode.HALF_UP
        return mRound.format(value)
    }

    @JvmStatic
    fun toastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}