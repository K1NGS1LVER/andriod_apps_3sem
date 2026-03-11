package com.danielpaul.financetracker.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyUtils {

    private fun getFormat(): NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    fun Double.toCurrencyString(): String = getFormat().format(this)

    fun Double.toCompactCurrencyString(): String {
        val format = getFormat()
        val symbol = try {
            format.currency?.symbol ?: "$"
        } catch (e: Exception) {
            "$"
        }
        return when {
            this >= 1_000_000 -> "$symbol${String.format("%.1f", this / 1_000_000)}M"
            this >= 1_000 -> "$symbol${String.format("%.1f", this / 1_000)}K"
            else -> format.format(this)
        }
    }
}
