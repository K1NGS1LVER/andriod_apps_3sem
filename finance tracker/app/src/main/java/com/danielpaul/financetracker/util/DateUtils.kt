package com.danielpaul.financetracker.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private fun getDisplayFormat() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private fun getMonthFormat() = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private fun getMonthDisplayFormat() = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    fun Long.toDisplayDate(): String = getDisplayFormat().format(Date(this))

    fun Long.toYearMonth(): String = getMonthFormat().format(Date(this))

    fun Long.toMonthDisplay(): String = getMonthDisplayFormat().format(Date(this))

    fun todayStartOfDay(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun isSameDay(date1: Long, date2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = date1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isYesterday(previousDate: Long, today: Long): Boolean {
        val yesterday = Calendar.getInstance().apply {
            timeInMillis = today
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis
        return isSameDay(previousDate, yesterday)
    }
}
