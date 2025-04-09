package com.ahmer.accounts.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateUtils {

    private val chartFormatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(Constants.PATTERN_CHART)

    private val englishLocale = Locale.ENGLISH

    /**
     * Returns the full name of the day of the week for the given date string.
     * If the date cannot be parsed, uses the current date.
     *
     * @param dateString The date string to parse.
     * @return The full name (capitalized) of the day.
     */
    fun actualDayOfWeek(dateString: String): String {
        return tryParseDate(dateString = dateString).dayOfWeek
            .getDisplayName(TextStyle.FULL, englishLocale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }

    /**
     * Returns a list of date strings representing each day of the month based on the provided date string.
     *
     * @param dateString The date string indicating the month.
     * @return A list of formatted date strings for each day in that month.
     */
    fun getMonthDates(dateString: String): List<String> {
        val baseDate = tryParseDate(dateString = dateString)
        val firstDay = baseDate.withDayOfMonth(1)
        return (0 until baseDate.lengthOfMonth())
            .map { firstDay.plusDays(it.toLong()) }
            .map { it.format(chartFormatter) }
    }

    /**
     * Returns the formatted first day of the month for the given date string.
     *
     * @param dateString The date string to parse.
     * @return Formatted date string representing the first day of the month.
     */
    fun firstDayOfMonth(dateString: String): String {
        return tryParseDate(dateString = dateString)
            .withDayOfMonth(1)
            .format(chartFormatter)
    }

    /**
     * Returns a list of formatted date strings between the start and end dates (inclusive).
     * If parsing fails for either date, an empty list is returned.
     *
     * @param startDate The start date string.
     * @param endDate The end date string.
     * @return A list of formatted dates within the range.
     */
    fun datesBetween(startDate: String, endDate: String): List<String> {
        return try {
            val start = LocalDate.parse(startDate, chartFormatter)
            val end = LocalDate.parse(endDate, chartFormatter)
            generateSequence(start) { it.plusDays(1) }
                .takeWhile { !it.isAfter(end) }
                .map { it.format(chartFormatter) }
                .toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Returns the formatted date string for the day before the provided date string.
     *
     * @param dateString The date string to parse.
     * @return Formatted date string for the previous day.
     */
    fun previousDay(dateString: String): String {
        return tryParseDate(dateString = dateString)
            .minusDays(1)
            .format(chartFormatter)
    }

    /**
     * Formats the given LocalDate using the chart formatter.
     *
     * @param date The LocalDate to format.
     * @return A formatted date string.
     */
    fun formatDate(date: LocalDate): String {
        return date.format(chartFormatter)
    }

    /**
     * Parses the provided date string using the chart formatter.
     * If parsing fails, the current date is returned.
     *
     * @param dateString The date string to parse.
     * @return The parsed LocalDate, or the current date if parsing fails.
     */
    private fun tryParseDate(dateString: String): LocalDate {
        return try {
            LocalDate.parse(dateString, chartFormatter)
        } catch (e: DateTimeParseException) {
            LocalDate.now()
        }
    }

    /**
     * Returns a list of formatted date strings representing one week, starting from
     * the Monday of the week containing the provided date string.
     *
     * @param dateString The date string to parse.
     * @return A list of formatted dates for the week.
     */
    fun getWeekDates(dateString: String): List<String> {
        val baseDate = tryParseDate(dateString = dateString)
        val startDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (0..6).map { startDate.plusDays(it.toLong()) }
            .map { it.format(chartFormatter) }
    }

    /**
     * Returns a list of formatted date strings for the past week.
     * The past week is defined as the week preceding the most recent Monday.
     *
     * @return A list of dates representing the past week.
     */
    fun getPastWeekData(): List<String> {
        val endDate = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        val startDate = endDate.minusWeeks(1)
        return datesBetween(startDate.format(chartFormatter), endDate.format(chartFormatter))
    }
}