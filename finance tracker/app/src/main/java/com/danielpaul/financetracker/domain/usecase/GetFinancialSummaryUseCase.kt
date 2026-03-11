package com.danielpaul.financetracker.domain.usecase

import com.danielpaul.financetracker.domain.model.FinancialSummary
import com.danielpaul.financetracker.domain.model.TransactionType
import com.danielpaul.financetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFinancialSummaryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<FinancialSummary> =
        repository.getAllTransactions().map { transactions ->
            val income = transactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }
            val expense = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
            val categoryBreakdown = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.category }
                .mapValues { (_, list) -> list.sumOf { it.amount } }
            FinancialSummary(
                totalBalance = income - expense,
                totalIncome = income,
                totalExpenses = expense,
                categoryBreakdown = categoryBreakdown
            )
        }
}
