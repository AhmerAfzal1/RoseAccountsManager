package com.ahmer.accounts.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun actualDayOfWeek(dateString: String): String {
        val format = SimpleDateFormat(Constants.PATTERN_CHART, Locale.ENGLISH)
        val date = format.parse(dateString)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> ""
        }
    }

    fun getWeekDates(dateString: String): List<String> {
        val dateFormat = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DATE, -dayOfWeek + 1)
        val weekDates = mutableListOf<String>()
        for (i in 1..7) {
            weekDates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return weekDates
    }

    fun getMonthDates(dateString: String): List<String> {
        val dateFormat = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val monthDates = mutableListOf<String>()
        for (i in 1..daysInMonth) {
            monthDates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return monthDates
    }

    fun firstDayOfMonth(date: String): String {
        val formatter = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val parsedDate = formatter.parse(date)
        val calendar = Calendar.getInstance()
        if (parsedDate != null) {
            calendar.time = parsedDate
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return formatter.format(calendar.time)
    }

    fun datesBetween(startDate: String, endDate: String): List<String> {
        val dateFormat = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val start = dateFormat.parse(startDate) ?: return emptyList()
        val end = dateFormat.parse(endDate) ?: return emptyList()
        val dataList = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        calendar.time = start

        while (calendar.time <= end) {
            dataList.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return dataList
    }

    fun getPastWeekData(): List<String> {
        val dateFormat = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)

        val data = mutableListOf<String>()
        for (i in 0 until 7) {
            data.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return data
    }

    fun previousDay(date: String): String {
        val formatter = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val parsedDate = formatter.parse(date)
        val calendar = Calendar.getInstance()
        if (parsedDate != null) {
            calendar.time = parsedDate
        }
        calendar.add(Calendar.DATE, -1)
        return formatter.format(calendar.time)
    }

    fun generateFormatDate(date: LocalDate): String {
        val dateCount =
            if (date.dayOfMonth < 10) "0${date.dayOfMonth}" else date.dayOfMonth.toString()
        val monthCount =
            if (date.monthValue < 10) "0${date.monthValue}" else date.monthValue.toString()
        return "${dateCount}/${monthCount}/${date.year}"
    }
}