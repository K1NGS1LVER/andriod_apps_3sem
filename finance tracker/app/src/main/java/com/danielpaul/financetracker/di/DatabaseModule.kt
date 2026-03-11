package com.danielpaul.financetracker.di

import android.content.Context
import androidx.room.Room
import com.danielpaul.financetracker.data.local.FinanceDatabase
import com.danielpaul.financetracker.data.local.FinancialGoalDao
import com.danielpaul.financetracker.data.local.StreakDao
import com.danielpaul.financetracker.data.local.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFinanceDatabase(@ApplicationContext context: Context): FinanceDatabase =
        Room.databaseBuilder(
            context,
            FinanceDatabase::class.java,
            "finance_tracker.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideTransactionDao(db: FinanceDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideStreakDao(db: FinanceDatabase): StreakDao = db.streakDao()

    @Provides
    fun provideFinancialGoalDao(db: FinanceDatabase): FinancialGoalDao = db.financialGoalDao()
}
