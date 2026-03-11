package com.danielpaul.financetracker.data.repository

import com.danielpaul.financetracker.data.local.FinancialGoalDao
import com.danielpaul.financetracker.data.local.FinancialGoalEntity
import com.danielpaul.financetracker.domain.model.FinancialGoal
import com.danielpaul.financetracker.domain.repository.FinancialGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FinancialGoalRepositoryImpl @Inject constructor(
    private val dao: FinancialGoalDao
) : FinancialGoalRepository {

    override fun getAllGoals(): Flow<List<FinancialGoal>> =
        dao.getAllGoals().map { it.map(FinancialGoalEntity::toDomain) }

    override suspend fun upsertGoal(goal: FinancialGoal) = dao.upsertGoal(goal.toEntity())

    override suspend fun deleteGoal(goal: FinancialGoal) = dao.deleteGoal(goal.toEntity())
}

private fun FinancialGoalEntity.toDomain(): FinancialGoal = FinancialGoal(
    id = id, label = label,
    targetAmount = targetAmount, currentAmount = currentAmount, period = period
)

private fun FinancialGoal.toEntity(): FinancialGoalEntity = FinancialGoalEntity(
    id = id, label = label,
    targetAmount = targetAmount, currentAmount = currentAmount, period = period
)
