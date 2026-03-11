package com.danielpaul.financetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.domain.model.TransactionType
import com.danielpaul.financetracker.domain.repository.TransactionRepository
import com.danielpaul.financetracker.domain.usecase.AddTransactionUseCase
import com.danielpaul.financetracker.domain.usecase.UpdateStreakUseCase
import com.danielpaul.financetracker.domain.usecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTransactionUiState(
    val amount: String = "",
    val category: String = "",
    val note: String = "",
    val date: Long = System.currentTimeMillis(),
    val type: TransactionType = TransactionType.EXPENSE,
    val isAmountError: Boolean = false,
    val isCategoryError: Boolean = false,
    val isSaved: Boolean = false,
    val editingTransaction: Transaction? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    fun loadTransaction(id: Int) {
        if (id == -1) return
        viewModelScope.launch {
            repository.getTransactionById(id)?.let { transaction ->
                _uiState.update {
                    it.copy(
                        amount = transaction.amount.toString(),
                        category = transaction.category,
                        note = transaction.note,
                        date = transaction.date,
                        type = transaction.type,
                        editingTransaction = transaction
                    )
                }
            }
        }
    }

    fun onAmountChange(value: String) =
        _uiState.update { it.copy(amount = value, isAmountError = false) }

    fun onCategoryChange(value: String) =
        _uiState.update { it.copy(category = value, isCategoryError = false) }

    fun onNoteChange(value: String) =
        _uiState.update { it.copy(note = value) }

    fun onDateChange(value: Long) =
        _uiState.update { it.copy(date = value) }

    fun onTypeChange(value: TransactionType) =
        _uiState.update { it.copy(type = value, category = "") }

    fun save() {
        val state = _uiState.value
        val amount = state.amount.toDoubleOrNull()
        var hasError = false

        if (amount == null || amount <= 0.0) {
            _uiState.update { it.copy(isAmountError = true) }
            hasError = true
        }
        if (state.category.isBlank()) {
            _uiState.update { it.copy(isCategoryError = true) }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            val transaction = Transaction(
                id = state.editingTransaction?.id ?: 0,
                amount = amount!!,
                category = state.category,
                note = state.note,
                date = state.date,
                type = state.type
            )
            if (state.editingTransaction != null) {
                updateTransactionUseCase(transaction)
            } else {
                addTransactionUseCase(transaction)
                updateStreakUseCase()
            }
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun resetSaved() {
        _uiState.update { it.copy(isSaved = false) }
    }

    fun resetState() {
        _uiState.value = AddTransactionUiState()
    }
}
