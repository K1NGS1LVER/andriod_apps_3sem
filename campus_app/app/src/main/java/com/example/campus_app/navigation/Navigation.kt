package com.example.campus_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.campus_app.ui.screens.DepartmentsScreen
import com.example.campus_app.ui.screens.EventDetailScreen
import com.example.campus_app.ui.screens.EventDetailsScreen
import com.example.campus_app.ui.screens.HomeScreen
import com.example.campus_app.ui.screens.NotificationsScreen
import com.example.campus_app.ui.screens.ProfileScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen()
        }
        composable(Screen.Departments.route) {
            DepartmentsScreen(onNavigateToEventDetails = { navController.navigate(Screen.EventDetails.createRoute("dummy")) })
        }
        composable(Screen.EventDetails.route) {
            val eventId = it.arguments?.getString("eventId") ?: ""
            EventDetailScreen(eventId)
        }
    }
}