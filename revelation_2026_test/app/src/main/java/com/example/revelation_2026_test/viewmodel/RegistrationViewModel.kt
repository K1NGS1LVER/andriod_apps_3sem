package com.example.revelation_2026_test.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.revelation_2026_test.model.RegistrationData

/**
 * ViewModel for managing registration state and validation across all screens.
 */
class RegistrationViewModel : ViewModel() {

    // Mutable state holding all registration data
    var registrationData by mutableStateOf(RegistrationData())
        private set

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE FUNCTIONS
    // ─────────────────────────────────────────────────────────────────────────

    fun updateName(name: String) {
        registrationData = registrationData.copy(name = name)
    }

    fun updateMobile(mobile: String) {
        // Only allow digits, max 10 characters
        if (mobile.all { it.isDigit() } && mobile.length <= 10) {
            registrationData = registrationData.copy(mobile = mobile)
        }
    }

    fun updateEmail(email: String) {
        registrationData = registrationData.copy(email = email)
    }

    fun updateCollegeName(collegeName: String) {
        registrationData = registrationData.copy(collegeName = collegeName)
    }

    fun updateGroupName(groupName: String) {
        registrationData = registrationData.copy(groupName = groupName)
    }

    fun updateNumberOfEvents(count: Int) {
        if (count in 1..5) {
            registrationData = registrationData.copy(numberOfEvents = count)
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VALIDATION FUNCTIONS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Name must not be blank and have at least 3 characters.
     */
    fun isNameValid(): Boolean {
        return registrationData.name.isNotBlank() && registrationData.name.length >= 3
    }

    /**
     * Mobile must be exactly 10 digits.
     */
    fun isMobileValid(): Boolean {
        return registrationData.mobile.length == 10 && registrationData.mobile.all { it.isDigit() }
    }

    /**
     * Email must match valid email pattern.
     */
    fun isEmailValid(): Boolean {
        return registrationData.email.isNotBlank() &&
               Patterns.EMAIL_ADDRESS.matcher(registrationData.email).matches()
    }

    /**
     * College name must not be blank.
     */
    fun isCollegeNameValid(): Boolean {
        return registrationData.collegeName.isNotBlank()
    }

    /**
     * Group name must be selected (not blank).
     */
    fun isGroupSelected(): Boolean {
        return registrationData.groupName.isNotBlank()
    }

    /**
     * Number of events must be at least 1.
     */
    fun isEventCountValid(): Boolean {
        return registrationData.numberOfEvents >= 1
    }

    /**
     * Check if the entire registration form (Screen 2) is valid.
     */
    fun isRegistrationFormValid(): Boolean {
        return isNameValid() && isMobileValid() && isEmailValid() && isCollegeNameValid()
    }

    /**
     * Check if event selection (Screen 3) is valid.
     */
    fun isEventSelectionValid(): Boolean {
        return isGroupSelected() && isEventCountValid()
    }

    /**
     * Reset all registration data to defaults.
     */
    fun resetRegistration() {
        registrationData = RegistrationData()
    }
}
