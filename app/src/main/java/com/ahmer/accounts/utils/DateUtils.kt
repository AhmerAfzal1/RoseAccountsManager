package com.ahmer.accounts.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    /**
     * Get date range between two dates
     *
     * @param startDate Start date - e.g week date
     * @param endDate End date - e.g today date
     * @return The list of dates
     */
    fun datesBetween(startDate: Long, endDate: Long): List<Long> {
        val dates = mutableListOf<Long>()
        var currentDate = startDate
        while (currentDate <= endDate) {
            dates.add(currentDate)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = currentDate
                add(Calendar.DATE, 1)
            }
            currentDate = calendar.timeInMillis
        }
        return dates
    }

    /**
     * Get the first day of the current month
     *
     * @return Time in milliseconds as [Long] of the first day of the month
     */
    fun firstDayOfMonth(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    /**
     * Get list of dates current month
     *
     * @return A list of dates for the current month
     */
    fun monthDates(): List<Long> {
        val oneDay: Long = 24 * 60 * 60 * 1000
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonthInMillis = calendar.timeInMillis
        calendar.set(Calendar.MONTH, currentMonth + 1)
        val lastDayOfMonthInMillis = calendar.timeInMillis - oneDay
        return (firstDayOfMonthInMillis..lastDayOfMonthInMillis step oneDay).toList()
    }

    /**
     * Get one week earlier date from the [startDate]
     *
     * @param startDate Start date in milliseconds
     * @return The updated one week earlier calendar date as a Long value
     */
    fun oneWeekEarlierDate(startDate: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = startDate
            add(Calendar.DATE, -6)
        }
        return calendar.timeInMillis
    }

    /**
     * Get start of the day based on the given time in milliseconds
     *
     * @param timeMillis Time in milliseconds
     * @return Time in milliseconds representing the start of the day
     */
    fun startOfDay(timeMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * Get dates of the week starting from the given [startDate]
     *
     * @param startDate Start date
     * @return List containing the dates of the week
     */
    fun weekDates(startDate: Long): List<Long> {
        val weekDates = mutableListOf<Long>()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startDate

        // Set the calendar to the start of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)

        // Add the start date to the list
        weekDates.add(calendar.timeInMillis)

        // Add the remaining dates of the week
        for (i in 1 until 7) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
            weekDates.add(calendar.timeInMillis)
        }
        return weekDates
    }

    fun actualDayOfWeek(dateString: String): String {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
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
}