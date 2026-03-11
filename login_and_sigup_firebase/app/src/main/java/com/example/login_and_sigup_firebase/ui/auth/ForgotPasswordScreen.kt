package com.example.login_and_sigup_firebase.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login_and_sigup_firebase.utils.Resource
import com.example.login_and_sigup_firebase.ui.components.LoadingScreen
import com.example.login_and_sigup_firebase.ui.components.ErrorDialog
import com.example.login_and_sigup_firebase.utils.ValidationUtils

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val resetState by viewModel.resetPasswordState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Reset Password", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Enter your email address and we will send you a link to reset your password.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.resetPassword(email) },
            modifier = Modifier.fillMaxWidth(),
            enabled = ValidationUtils.isValidEmail(email)
        ) {
            Text("Send Reset Link")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Back to Login")
        }

        when (resetState) {
            is Resource.Loading -> LoadingScreen()
            is Resource.Success -> {
                AlertDialog(
                    onDismissRequest = onNavigateBack,
                    confirmButton = {
                        TextButton(onClick = onNavigateBack) {
                            Text("OK")
                        }
                    },
                    title = { Text("Success") },
                    text = { Text("Password reset email sent successfully.") }
                )
            }
            is Resource.Error -> {
                ErrorDialog((resetState as Resource.Error).message) {
                    // Reset state
                }
            }
            else -> {}
        }
    }
}
