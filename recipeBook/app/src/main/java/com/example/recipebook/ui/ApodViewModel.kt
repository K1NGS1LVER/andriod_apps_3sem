package com.example.recipebook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipebook.data.model.ApodDto
import com.example.recipebook.data.repository.ApodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class ApodUiState {
    object Loading : ApodUiState()
    data class Success(val apods: List<ApodDto>) : ApodUiState()
    data class Error(val message: String) : ApodUiState()
}

class ApodViewModel(private val repository: ApodRepository) : ViewModel() {

    private val _rawState = MutableStateFlow<ApodUiState>(ApodUiState.Loading)

    // Search query — updated by the UI
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Derived state: apply search filter on top of raw API state
    val uiState: StateFlow<ApodUiState> = combine(_rawState, _searchQuery) { state, query ->
        when {
            state !is ApodUiState.Success -> state
            query.isBlank()              -> state
            else -> {
                val q = query.trim().lowercase()
                val filtered = state.apods.filter { apod ->
                    apod.title.lowercase().contains(q) ||
                    apod.date.contains(q) ||
                    apod.explanation.lowercase().contains(q)
                }
                ApodUiState.Success(filtered)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ApodUiState.Loading
    )

    init {
        fetchLastTenDays()
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun fetchLastTenDays() {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val tenDaysAgo = today.minusDays(9)
        val startDate = tenDaysAgo.format(formatter)
        val endDate = today.format(formatter)

        viewModelScope.launch {
            _rawState.value = ApodUiState.Loading
            repository.getLastTenDaysApod(startDate, endDate).collect { result ->
                _rawState.value = result.fold(
                    onSuccess = { ApodUiState.Success(it) },
                    onFailure = { ApodUiState.Error(it.message ?: "Unknown error occurred.") }
                )
            }
        }
    }

    fun getApodByDate(date: String): ApodDto? {
        return (_rawState.value as? ApodUiState.Success)?.apods?.find { it.date == date }
    }

    // --- Factory for manual DI ---
    class Factory(private val repository: ApodRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ApodViewModel(repository) as T
        }
    }
}
