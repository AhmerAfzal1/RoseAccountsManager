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
        val formatter = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val start = formatter.parse(startDate)
        val end = formatter.parse(endDate)
        val dates = mutableListOf<String>()
        var currentDate = start

        while (currentDate!! <= end) {
            dates.add(formatter.format(currentDate))
            val calendar = Calendar.getInstance().apply {
                time = currentDate!!
                add(Calendar.DATE, 1)
            }
            currentDate = calendar.time
        }

        return dates
    }

    fun generate7daysPriorDate(date: String): String {
        val formatter = SimpleDateFormat(Constants.PATTERN_CHART, Locale.getDefault())
        val parsedDate = formatter.parse(date)
        val calendar = Calendar.getInstance()
        if (parsedDate != null) {
            calendar.time = parsedDate
        }
        calendar.add(Calendar.DATE, -6)
        return formatter.format(calendar.time)
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