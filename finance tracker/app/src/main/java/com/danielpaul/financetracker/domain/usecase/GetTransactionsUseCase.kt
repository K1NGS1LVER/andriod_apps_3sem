package com.danielpaul.financetracker.domain.usecase

import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getAllTransactions()

    fun getRecent(limit: Int = 5): Flow<List<Transaction>> =
        repository.getRecentTransactions(limit)

    fun getByMonth(yearMonth: String): Flow<List<Transaction>> =
        repository.getTransactionsByMonth(yearMonth)
}
