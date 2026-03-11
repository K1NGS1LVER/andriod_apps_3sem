package com.danielpaul.financetracker.domain.usecase

import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) =
        repository.deleteTransaction(transaction)
}
