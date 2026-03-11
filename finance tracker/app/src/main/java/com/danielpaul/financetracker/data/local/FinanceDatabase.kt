package com.danielpaul.financetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TransactionEntity::class, StreakEntity::class, FinancialGoalEntity::class],
    version = 2,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun streakDao(): StreakDao
    abstract fun financialGoalDao(): FinancialGoalDao
}
