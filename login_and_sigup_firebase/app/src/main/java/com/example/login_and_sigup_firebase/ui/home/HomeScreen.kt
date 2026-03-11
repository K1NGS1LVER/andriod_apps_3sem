package com.example.login_and_sigup_firebase.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login_and_sigup_firebase.ui.auth.AuthViewModel
import com.example.login_and_sigup_firebase.utils.Resource
import com.example.login_and_sigup_firebase.ui.components.LoadingScreen
import com.example.login_and_sigup_firebase.ui.components.ErrorDialog

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserDetails()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (userState) {
            is Resource.Loading -> LoadingScreen()
            is Resource.Success -> {
                val user = (userState as Resource.Success).data
                Text("Welcome,", fontSize = 20.sp)
                Text(user.fullName, fontSize = 32.sp, style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(user.email, style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(onClick = {
                    viewModel.logout()
                    onLogout()
                }) {
                    Text("Logout")
                }
            }
            is Resource.Error -> {
                Text("Failed to load user details")
                Button(onClick = { viewModel.getUserDetails() }) {
                    Text("Retry")
                }
                ErrorDialog((userState as Resource.Error).message) {
                    // Reset or handle
                }
            }
            else -> {
                Text("No user data")
                Button(onClick = {
                    viewModel.logout()
                    onLogout()
                }) {
                    Text("Logout")
                }
            }
        }
    }
}
