package com.danielpaul.financetracker.domain.repository

import com.danielpaul.financetracker.domain.model.FinancialGoal
import kotlinx.coroutines.flow.Flow

interface FinancialGoalRepository {
    fun getAllGoals(): Flow<List<FinancialGoal>>
    suspend fun upsertGoal(goal: FinancialGoal)
    suspend fun deleteGoal(goal: FinancialGoal)
}
