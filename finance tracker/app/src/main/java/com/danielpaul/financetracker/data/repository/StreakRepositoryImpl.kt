package com.danielpaul.financetracker.data.repository

import com.danielpaul.financetracker.data.local.StreakDao
import com.danielpaul.financetracker.data.local.StreakEntity
import com.danielpaul.financetracker.domain.model.Streak
import com.danielpaul.financetracker.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val dao: StreakDao
) : StreakRepository {

    override fun getStreak(): Flow<Streak?> = dao.getStreak().map { it?.toDomain() }

    override suspend fun updateStreak(streak: Streak) = dao.upsertStreak(streak.toEntity())

    override suspend fun getStreakOnce(): Streak? = dao.getStreakOnce()?.toDomain()
}

private fun StreakEntity.toDomain(): Streak = Streak(
    id = id, currentStreak = currentStreak,
    longestStreak = longestStreak, lastEntryDate = lastEntryDate
)

private fun Streak.toEntity(): StreakEntity = StreakEntity(
    id = id, currentStreak = currentStreak,
    longestStreak = longestStreak, lastEntryDate = lastEntryDate
)
