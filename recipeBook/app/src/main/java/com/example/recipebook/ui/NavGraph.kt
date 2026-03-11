package com.example.recipebook.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipebook.di.AppModule

private const val ROUTE_LIST   = "list"
private const val ROUTE_DETAIL = "detail/{date}"

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val viewModel: ApodViewModel = viewModel(
        factory = ApodViewModel.Factory(AppModule.repository)
    )
    val uiState     by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    NavHost(navController = navController, startDestination = ROUTE_LIST) {

        composable(ROUTE_LIST) {
            ApodListScreen(
                uiState       = uiState,
                searchQuery   = searchQuery,
                onSearchChange = { viewModel.updateSearch(it) },
                onCardClick   = { apod -> navController.navigate("detail/${apod.date}") },
                onRetry       = { viewModel.fetchLastTenDays() }
            )
        }

        composable(
            route     = ROUTE_DETAIL,
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val apod = viewModel.getApodByDate(date)

            ApodDetailScreen(
                apod   = apod,
                onBack = { navController.navigateUp() }
            )
        }
    }
}
