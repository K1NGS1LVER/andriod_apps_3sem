package com.danielpaul.financetracker.domain.model

data class FinancialGoal(
    val id: Int = 0,
    val label: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val period: String = "Monthly"
)
