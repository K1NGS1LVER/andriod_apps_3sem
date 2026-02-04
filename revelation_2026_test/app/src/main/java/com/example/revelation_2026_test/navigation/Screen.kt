package com.example.revelation_2026_test.navigation

/**
 * Sealed class defining all navigation routes for the app.
 * Each screen has a unique route string for NavController navigation.
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object Registration : Screen("registration")
    data object EventSelection : Screen("event_selection")
    data object Confirmation : Screen("confirmation")
}
