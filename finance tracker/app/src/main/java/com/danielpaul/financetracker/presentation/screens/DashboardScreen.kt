package com.danielpaul.financetracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.danielpaul.financetracker.presentation.components.AnimatedBalanceText
import com.danielpaul.financetracker.presentation.components.EmptyState
import com.danielpaul.financetracker.presentation.components.ProgressCard
import com.danielpaul.financetracker.presentation.components.StreakIndicator
import com.danielpaul.financetracker.presentation.components.SummaryCard
import com.danielpaul.financetracker.presentation.components.TransactionCard
import com.danielpaul.financetracker.presentation.viewmodel.DashboardViewModel
import com.danielpaul.financetracker.theme.AccentTeal
import com.danielpaul.financetracker.theme.ExpenseRed
import com.danielpaul.financetracker.theme.IncomeGreen
import com.danielpaul.financetracker.theme.PrimaryNavy
import com.danielpaul.financetracker.theme.PrimaryNavyLight

@Composable
fun DashboardScreen(
    onNavigateToAddTransaction: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTransaction,
                containerColor = AccentTeal
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentTeal)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Header with balance
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryNavy)
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                    ) {
                        Column {
                            Text(
                                text = "Total Balance",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AccentTeal
                            )
                            Spacer(Modifier.height(8.dp))
                            AnimatedBalanceText(
                                amount = state.summary.totalBalance,
                                style = MaterialTheme.typography.displayMedium,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }

                // Summary Cards
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryNavyLight)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            label = "Income",
                            amount = state.summary.totalIncome,
                            icon = Icons.Rounded.TrendingUp,
                            iconTint = IncomeGreen,
                            containerColor = PrimaryNavy,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            label = "Expenses",
                            amount = state.summary.totalExpenses,
                            icon = Icons.Rounded.TrendingDown,
                            iconTint = ExpenseRed,
                            containerColor = PrimaryNavy,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Streak Indicator
                state.streak?.let { streak ->
                    item {
                        StreakIndicator(
                            streak = streak,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                // Goals
                if (state.goals.isNotEmpty()) {
                    item {
                        Text(
                            text = "Goals",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                    items(state.goals) { goal ->
                        ProgressCard(
                            goal = goal,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }

                // Recent Transactions Header
                item {
                    Text(
                        text = "Recent Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                if (state.recentTransactions.isEmpty()) {
                    item {
                        EmptyState(
                            message = "No transactions yet",
                            subtitle = "Tap + to add your first transaction"
                        )
                    }
                } else {
                    items(state.recentTransactions, key = { it.id }) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
