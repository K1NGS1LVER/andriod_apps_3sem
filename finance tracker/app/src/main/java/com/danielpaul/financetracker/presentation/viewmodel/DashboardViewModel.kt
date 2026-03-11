package com.danielpaul.financetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpaul.financetracker.domain.model.FinancialGoal
import com.danielpaul.financetracker.domain.model.FinancialSummary
import com.danielpaul.financetracker.domain.model.Streak
import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.domain.usecase.GetFinancialSummaryUseCase
import com.danielpaul.financetracker.domain.usecase.GetGoalsUseCase
import com.danielpaul.financetracker.domain.usecase.GetStreakUseCase
import com.danielpaul.financetracker.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val summary: FinancialSummary = FinancialSummary(0.0, 0.0, 0.0, emptyMap()),
    val recentTransactions: List<Transaction> = emptyList(),
    val streak: Streak? = null,
    val goals: List<FinancialGoal> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    getTransactionsUseCase: GetTransactionsUseCase,
    getStreakUseCase: GetStreakUseCase,
    getGoalsUseCase: GetGoalsUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        getFinancialSummaryUseCase(),
        getTransactionsUseCase.getRecent(5),
        getStreakUseCase(),
        getGoalsUseCase()
    ) { summary, recent, streak, goals ->
        DashboardUiState(
            summary = summary,
            recentTransactions = recent,
            streak = streak,
            goals = goals,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )
}
