package com.example.login_and_sigup_firebase.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login_and_sigup_firebase.data.model.User
import com.example.login_and_sigup_firebase.data.repository.AuthRepository
import com.example.login_and_sigup_firebase.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<Boolean>?>(null)
    val loginState: StateFlow<Resource<Boolean>?> = _loginState

    private val _signupState = MutableStateFlow<Resource<Boolean>?>(null)
    val signupState: StateFlow<Resource<Boolean>?> = _signupState

    private val _resetPasswordState = MutableStateFlow<Resource<Boolean>?>(null)
    val resetPasswordState: StateFlow<Resource<Boolean>?> = _resetPasswordState

    private val _userState = MutableStateFlow<Resource<User>?>(null)
    val userState: StateFlow<Resource<User>?> = _userState

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = Resource.Loading
        _loginState.value = repository.login(email, password)
    }

    fun signUp(name: String, email: String, password: String) = viewModelScope.launch {
        _signupState.value = Resource.Loading
        _signupState.value = repository.signUp(name, email, password)
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _resetPasswordState.value = Resource.Loading
        _resetPasswordState.value = repository.resetPassword(email)
    }

    fun logout() {
        repository.logout()
        _userState.value = null
        _loginState.value = null
        _signupState.value = null
    }

    fun getUserDetails() {
        val uid = currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                repository.getUserDetails(uid).collect {
                    _userState.value = it
                }
            }
        }
    }
}
