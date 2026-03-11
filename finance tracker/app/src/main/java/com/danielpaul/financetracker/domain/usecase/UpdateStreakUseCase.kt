package com.danielpaul.financetracker.domain.usecase

import com.danielpaul.financetracker.domain.model.Streak
import com.danielpaul.financetracker.domain.repository.StreakRepository
import com.danielpaul.financetracker.util.DateUtils
import javax.inject.Inject

class UpdateStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    suspend operator fun invoke() {
        val today = DateUtils.todayStartOfDay()
        val current = repository.getStreakOnce()

        if (current == null) {
            repository.updateStreak(Streak(currentStreak = 1, longestStreak = 1, lastEntryDate = today))
            return
        }

        when {
            DateUtils.isSameDay(current.lastEntryDate, today) -> {
                // Already logged today – no change
            }
            DateUtils.isYesterday(current.lastEntryDate, today) -> {
                val newStreak = current.currentStreak + 1
                repository.updateStreak(
                    current.copy(
                        currentStreak = newStreak,
                        longestStreak = maxOf(newStreak, current.longestStreak),
                        lastEntryDate = today
                    )
                )
            }
            else -> {
                // Streak broken – reset
                repository.updateStreak(
                    current.copy(
                        currentStreak = 1,
                        longestStreak = maxOf(1, current.longestStreak),
                        lastEntryDate = today
                    )
                )
            }
        }
    }
}
