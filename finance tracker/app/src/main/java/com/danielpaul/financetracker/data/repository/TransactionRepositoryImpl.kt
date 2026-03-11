package com.danielpaul.financetracker.data.repository

import com.danielpaul.financetracker.data.local.TransactionDao
import com.danielpaul.financetracker.data.local.TransactionEntity
import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.domain.model.TransactionType
import com.danielpaul.financetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        dao.getAllTransactions().map { it.map(TransactionEntity::toDomain) }

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> =
        dao.getRecentTransactions(limit).map { it.map(TransactionEntity::toDomain) }

    override fun getTransactionsByMonth(yearMonth: String): Flow<List<Transaction>> =
        dao.getTransactionsByMonth(yearMonth).map { it.map(TransactionEntity::toDomain) }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> =
        dao.getTransactionsByCategory(category).map { it.map(TransactionEntity::toDomain) }

    override suspend fun getTransactionById(id: Int): Transaction? =
        dao.getTransactionById(id)?.toDomain()

    override suspend fun insertTransaction(transaction: Transaction) =
        dao.insertTransaction(transaction.toEntity())

    override suspend fun updateTransaction(transaction: Transaction) =
        dao.updateTransaction(transaction.toEntity())

    override suspend fun deleteTransaction(transaction: Transaction) =
        dao.deleteTransaction(transaction.toEntity())
}

private fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id, amount = amount, category = category,
    note = note, date = date, type = TransactionType.valueOf(type)
)

private fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id, amount = amount, category = category,
    note = note, date = date, type = type.name
)
