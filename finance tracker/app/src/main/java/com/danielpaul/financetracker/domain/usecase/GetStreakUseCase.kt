package com.danielpaul.financetracker.domain.usecase

import com.danielpaul.financetracker.domain.model.Streak
import com.danielpaul.financetracker.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    operator fun invoke(): Flow<Streak?> = repository.getStreak()
}
