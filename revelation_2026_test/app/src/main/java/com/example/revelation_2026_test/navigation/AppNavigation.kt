package com.example.revelation_2026_test.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.revelation_2026_test.ui.screens.*
import com.example.revelation_2026_test.viewmodel.RegistrationViewModel

/**
 * Main navigation composable that sets up the NavHost with all screens.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: RegistrationViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(Screen.Registration.route)
                }
            )
        }

        // Registration Screen
        composable(Screen.Registration.route) {
            RegistrationScreen(
                viewModel = viewModel,
                onNextClick = {
                    navController.navigate(Screen.EventSelection.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Event Selection Screen
        composable(Screen.EventSelection.route) {
            EventSelectionScreen(
                viewModel = viewModel,
                onContinueClick = {
                    navController.navigate(Screen.Confirmation.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Confirmation Screen
        composable(Screen.Confirmation.route) {
            ConfirmationScreen(
                viewModel = viewModel,
                onEditClick = {
                    // Go back to registration to edit
                    navController.popBackStack(Screen.Registration.route, inclusive = false)
                },
                onDoneClick = {
                    // Navigate back to Welcome and clear the entire back stack
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
