package com.danielpaul.financetracker.domain.repository

import com.danielpaul.financetracker.domain.model.Streak
import kotlinx.coroutines.flow.Flow

interface StreakRepository {
    fun getStreak(): Flow<Streak?>
    suspend fun updateStreak(streak: Streak)
    suspend fun getStreakOnce(): Streak?
}
