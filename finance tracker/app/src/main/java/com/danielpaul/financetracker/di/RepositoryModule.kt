package com.danielpaul.financetracker.di

import com.danielpaul.financetracker.data.repository.FinancialGoalRepositoryImpl
import com.danielpaul.financetracker.data.repository.StreakRepositoryImpl
import com.danielpaul.financetracker.data.repository.TransactionRepositoryImpl
import com.danielpaul.financetracker.domain.repository.FinancialGoalRepository
import com.danielpaul.financetracker.domain.repository.StreakRepository
import com.danielpaul.financetracker.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindStreakRepository(
        impl: StreakRepositoryImpl
    ): StreakRepository

    @Binds
    @Singleton
    abstract fun bindFinancialGoalRepository(
        impl: FinancialGoalRepositoryImpl
    ): FinancialGoalRepository
}
