package com.christ.conferenceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.christ.conferenceapp.data.Attendee
import com.christ.conferenceapp.repository.AttendeeRepository
import kotlinx.coroutines.launch

class AttendeeViewModel(private val repository: AttendeeRepository) : ViewModel() {

    val attendees: LiveData<List<Attendee>> = repository.getAllAttendees()

    fun addAttendee(
        attendee: Attendee,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                repository.insertAttendee(attendee)
            }.onSuccess {
                onSuccess()
            }.onFailure(onError)
        }
    }

    fun updateAttendee(
        attendee: Attendee,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                repository.updateAttendee(attendee)
            }.onSuccess {
                onSuccess()
            }.onFailure(onError)
        }
    }

    fun deleteAttendee(
        attendee: Attendee,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                repository.deleteAttendee(attendee)
            }.onSuccess {
                onSuccess()
            }.onFailure(onError)
        }
    }

    class Factory(private val repository: AttendeeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(AttendeeViewModel::class.java)) {
                "Unknown ViewModel class: ${modelClass.name}"
            }
            return AttendeeViewModel(repository) as T
        }
    }
}
