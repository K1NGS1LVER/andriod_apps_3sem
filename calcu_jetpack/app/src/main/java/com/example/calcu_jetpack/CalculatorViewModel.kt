package com.example.calcu_jetpack

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> _state.value = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
            is CalculatorAction.ToggleSign -> toggleSign()
            is CalculatorAction.Percentage -> performPercentage()
        }
    }

    private fun enterNumber(number: Int) {
        if (_state.value.operation == null) {
            if (_state.value.number1.length >= MAX_NUM_LENGTH) return
            _state.value = _state.value.copy(number1 = _state.value.number1 + number)
            return
        }
        if (_state.value.number2.length >= MAX_NUM_LENGTH) return
        _state.value = _state.value.copy(number2 = _state.value.number2 + number)
    }

    private fun enterDecimal() {
        if (
            _state.value.operation == null &&
            !_state.value.number1.contains(".") &&
            _state.value.number1.isNotBlank()
        ) {
            _state.value = _state.value.copy(number1 = _state.value.number1 + ".")
            return
        }
        if (
            !_state.value.number2.contains(".") &&
            _state.value.number2.isNotBlank()
        ) {
            _state.value = _state.value.copy(number2 = _state.value.number2 + ".")
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (_state.value.number1.isNotBlank()) {
            _state.value = _state.value.copy(operation = operation)
        }
    }

    private fun performCalculation() {
        val number1 = _state.value.number1.toDoubleOrNull()
        val number2 = _state.value.number2.toDoubleOrNull()
        if (number1 != null && number2 != null) {
            val result = when (_state.value.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                null -> return
            }
            val historyEntry = "$number1 ${_state.value.operation?.symbol} $number2 = $result"
            _state.value = _state.value.copy(
                number1 = result.toString().take(15),
                number2 = "",
                operation = null,
                history = _state.value.history + historyEntry
            )
        }
    }

    private fun performDeletion() {
        when {
            _state.value.number2.isNotBlank() -> _state.value = _state.value.copy(
                number2 = _state.value.number2.dropLast(1)
            )
            _state.value.operation != null -> _state.value = _state.value.copy(
                operation = null
            )
            _state.value.number1.isNotBlank() -> _state.value = _state.value.copy(
                number1 = _state.value.number1.dropLast(1)
            )
        }
    }

    private fun toggleSign() {
        if (_state.value.number2.isNotBlank()) {
            val number = _state.value.number2.toDouble() * -1
            _state.value = _state.value.copy(number2 = number.toString())
        } else if (_state.value.number1.isNotBlank()) {
            val number = _state.value.number1.toDouble() * -1
            _state.value = _state.value.copy(number1 = number.toString())
        }
    }

    private fun performPercentage() {
        if (_state.value.number1.isNotBlank()) {
            val number = _state.value.number1.toDouble() / 100
            _state.value = _state.value.copy(number1 = number.toString())
        }
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}

data class CalculatorState(
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null,
    val history: List<String> = emptyList()
) {
    val display: String
        get() = when {
            number2.isNotBlank() -> number2
            number1.isNotBlank() -> number1
            else -> "0"
        }
}

sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Decimal : CalculatorAction()
    object Calculate : CalculatorAction()
    object ToggleSign : CalculatorAction()
    object Percentage : CalculatorAction()
    data class Operation(val operation: CalculatorOperation) : CalculatorAction()
}

sealed class CalculatorOperation(val symbol: String) {
    object Add : CalculatorOperation("+")
    object Subtract : CalculatorOperation("-")
    object Multiply : CalculatorOperation("×")
    object Divide : CalculatorOperation("÷")
}