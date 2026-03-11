package com.danielpaul.financetracker.domain.usecase

import com.danielpaul.financetracker.domain.model.FinancialGoal
import com.danielpaul.financetracker.domain.repository.FinancialGoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val repository: FinancialGoalRepository
) {
    operator fun invoke(): Flow<List<FinancialGoal>> = repository.getAllGoals()
}
