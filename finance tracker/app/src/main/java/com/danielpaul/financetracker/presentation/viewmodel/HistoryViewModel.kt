package com.danielpaul.financetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.domain.usecase.DeleteTransactionUseCase
import com.danielpaul.financetracker.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val allTransactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val selectedCategory: String = "All",
    val isLoading: Boolean = true
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")

    val uiState: StateFlow<HistoryUiState> = combine(
        getTransactionsUseCase(),
        _selectedCategory
    ) { transactions, category ->
        val filtered = if (category == "All") transactions
        else transactions.filter { it.category == category }
        HistoryUiState(
            allTransactions = transactions,
            filteredTransactions = filtered,
            selectedCategory = category,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState()
    )

    fun setCategory(category: String) { _selectedCategory.value = category }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { deleteTransactionUseCase(transaction) }
    }
}
