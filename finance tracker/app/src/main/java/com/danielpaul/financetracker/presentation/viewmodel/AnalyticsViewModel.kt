package com.danielpaul.financetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpaul.financetracker.domain.model.FinancialSummary
import com.danielpaul.financetracker.domain.model.TransactionType
import com.danielpaul.financetracker.domain.usecase.GetFinancialSummaryUseCase
import com.danielpaul.financetracker.domain.usecase.GetTransactionsUseCase
import com.danielpaul.financetracker.util.DateUtils.toYearMonth
import com.danielpaul.financetracker.util.DateUtils.toMonthDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MonthlyData(val month: String, val income: Double, val expense: Double)

data class AnalyticsUiState(
    val summary: FinancialSummary = FinancialSummary(0.0, 0.0, 0.0, emptyMap()),
    val monthlyData: List<MonthlyData> = emptyList(),
    val topCategory: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    val uiState: StateFlow<AnalyticsUiState> = combine(
        getFinancialSummaryUseCase(),
        getTransactionsUseCase()
    ) { summary, transactions ->
        val topCategory = summary.categoryBreakdown.maxByOrNull { it.value }?.key ?: ""

        val monthlyMap = mutableMapOf<String, Pair<Double, Double>>()
        transactions.forEach { t ->
            val key = t.date.toYearMonth()
            val current = monthlyMap.getOrDefault(key, 0.0 to 0.0)
            monthlyMap[key] = if (t.type == TransactionType.INCOME) {
                (current.first + t.amount) to current.second
            } else {
                current.first to (current.second + t.amount)
            }
        }

        val monthlyData = monthlyMap.entries
            .sortedBy { it.key }
            .takeLast(6)
            .map { (key, pair) ->
                val parts = key.split("-")
                val displayDate = if (parts.size == 2) {
                    "${parts[1]}/${parts[0].takeLast(2)}"
                } else key
                MonthlyData(displayDate, pair.first, pair.second)
            }

        AnalyticsUiState(
            summary = summary,
            monthlyData = monthlyData,
            topCategory = topCategory,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AnalyticsUiState()
    )
}
