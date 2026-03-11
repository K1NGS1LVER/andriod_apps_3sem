package com.danielpaul.financetracker.domain.model

data class Streak(
    val id: Int = 1,
    val currentStreak: Int,
    val longestStreak: Int,
    val lastEntryDate: Long
)
