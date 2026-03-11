package com.danielpaul.financetracker.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Dashboard : Screen("dashboard")
    data object AddTransaction : Screen("add_transaction?transactionId={transactionId}") {
        fun createRoute(transactionId: Int = -1) = "add_transaction?transactionId=$transactionId"
    }
    data object History : Screen("history")
    data object Analytics : Screen("analytics")
}
