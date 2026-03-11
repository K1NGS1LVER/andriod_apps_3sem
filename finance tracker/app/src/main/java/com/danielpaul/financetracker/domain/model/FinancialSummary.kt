package com.danielpaul.financetracker.domain.model

data class FinancialSummary(
    val totalBalance: Double,
    val totalIncome: Double,
    val totalExpenses: Double,
    val categoryBreakdown: Map<String, Double>
)
