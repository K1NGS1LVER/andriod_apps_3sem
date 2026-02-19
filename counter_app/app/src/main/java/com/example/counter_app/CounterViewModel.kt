package com.example.counter_app

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Tracks the last user action to drive UI changes (e.g. background swap). */
enum class LastAction { INCREMENT, DECREMENT }

/** A single entry in the history log. */
data class HistoryEntry(val label: String, val timestamp: String)

private const val PREFS_NAME = "counter_prefs"
private const val KEY_COUNT = "count"
private const val HISTORY_MAX = 10

/**
 * ViewModel for the Counter screen.
 *
 * – Persists the counter value across process kills via SharedPreferences.
 * – Enforces a minimum value of 0 (no negatives).
 * – Tracks the last action and a bounded history log.
 * – Supports configurable step sizes: 1, 5, or 10.
 * – Countdown timer with start / pause / reset.
 */
class CounterViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val fmt = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

    // ── Counter State ─────────────────────────────────────────────────────────

    private val _count = mutableStateOf(prefs.getInt(KEY_COUNT, 0))
    val count: State<Int> = _count

    private val _lastAction = mutableStateOf<LastAction?>(null)
    val lastAction: State<LastAction?> = _lastAction

    private val _stepSize = mutableStateOf(1)
    val stepSize: State<Int> = _stepSize

    val history = mutableStateListOf<HistoryEntry>()

    // ── Counter Actions ───────────────────────────────────────────────────────

    fun increment() {
        _count.value += _stepSize.value
        _lastAction.value = LastAction.INCREMENT
        addHistory("+${_stepSize.value}")
        persist()
    }

    fun decrement() {
        if (_count.value - _stepSize.value < 0) _count.value = 0
        else _count.value -= _stepSize.value
        _lastAction.value = LastAction.DECREMENT
        addHistory("-${_stepSize.value}")
        persist()
    }

    fun reset() {
        _count.value = 0
        _lastAction.value = null
        addHistory("Reset")
        persist()
    }

    fun setStepSize(step: Int) { _stepSize.value = step }

    // ── Countdown State ───────────────────────────────────────────────────────

    private val _countdownTarget    = mutableStateOf(10)
    val countdownTarget: State<Int> = _countdownTarget

    private val _countdownRemaining    = mutableStateOf(10)
    val countdownRemaining: State<Int> = _countdownRemaining

    private val _isCountdownRunning       = mutableStateOf(false)
    val isCountdownRunning: State<Boolean> = _isCountdownRunning

    private val _countdownFinished       = mutableStateOf(false)
    val countdownFinished: State<Boolean> = _countdownFinished

    private var countdownJob: Job? = null

    // ── Countdown Actions ─────────────────────────────────────────────────────

    /** Adjust the countdown target (only when not running). */
    fun setCountdownTarget(target: Int) {
        if (_isCountdownRunning.value) return
        val clamped = target.coerceIn(1, 9999)
        _countdownTarget.value = clamped
        _countdownRemaining.value = clamped
        _countdownFinished.value = false
    }

    /** Start or resume the countdown. */
    fun startCountdown() {
        if (_isCountdownRunning.value || _countdownRemaining.value <= 0) return
        _isCountdownRunning.value = true
        _countdownFinished.value = false
        countdownJob = viewModelScope.launch {
            while (_countdownRemaining.value > 0 && _isCountdownRunning.value) {
                delay(1000L)
                if (_isCountdownRunning.value) _countdownRemaining.value -= 1
            }
            if (_countdownRemaining.value <= 0) {
                _isCountdownRunning.value = false
                _countdownFinished.value = true
            }
        }
    }

    /** Pause a running countdown. */
    fun pauseCountdown() {
        countdownJob?.cancel()
        _isCountdownRunning.value = false
    }

    /** Reset countdown back to the set target. */
    fun resetCountdown() {
        countdownJob?.cancel()
        _isCountdownRunning.value = false
        _countdownRemaining.value = _countdownTarget.value
        _countdownFinished.value = false
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun addHistory(label: String) {
        history.add(0, HistoryEntry(label, fmt.format(Date())))
        if (history.size > HISTORY_MAX) history.removeAt(history.lastIndex)
    }

    private fun persist() {
        prefs.edit().putInt(KEY_COUNT, _count.value).apply()
    }
}
