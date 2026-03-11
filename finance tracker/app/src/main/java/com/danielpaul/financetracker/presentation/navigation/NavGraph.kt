package com.danielpaul.financetracker.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielpaul.financetracker.presentation.screens.AddTransactionScreen
import com.danielpaul.financetracker.presentation.screens.AnalyticsScreen
import com.danielpaul.financetracker.presentation.screens.DashboardScreen
import com.danielpaul.financetracker.presentation.screens.SplashScreen
import com.danielpaul.financetracker.presentation.screens.TransactionHistoryScreen

private data class BottomNavItem(
    val screen: Screen, val label: String, val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Dashboard, "Dashboard", Icons.Rounded.Dashboard),
    BottomNavItem(Screen.History, "History", Icons.Rounded.History),
    BottomNavItem(Screen.Analytics, "Analytics", Icons.Rounded.Analytics),
)

@Composable
fun FinanceNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = bottomNavItems.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy
                            ?.any { it.route == item.screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Wrap NavHost in a Box to isolate innerPadding from Navigation Transitions
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(onNavigateToDashboard = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    })
                }
                composable(Screen.Dashboard.route) {
                    DashboardScreen(onNavigateToAddTransaction = {
                        navController.navigate(Screen.AddTransaction.createRoute())
                    })
                }
                composable(
                    route = Screen.AddTransaction.route,
                    arguments = listOf(navArgument("transactionId") {
                        type = NavType.IntType; defaultValue = -1
                    })
                ) { backStackEntry ->
                    val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
                    AddTransactionScreen(
                        transactionId = transactionId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.History.route) {
                    TransactionHistoryScreen(onEditTransaction = { transaction ->
                        navController.navigate(Screen.AddTransaction.createRoute(transaction.id))
                    })
                }
                composable(Screen.Analytics.route) {
                    AnalyticsScreen()
                }
            }
        }
    }
}
