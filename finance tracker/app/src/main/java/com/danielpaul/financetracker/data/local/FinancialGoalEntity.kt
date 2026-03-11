package com.danielpaul.financetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_goals")
data class FinancialGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val period: String = "Monthly"
)
