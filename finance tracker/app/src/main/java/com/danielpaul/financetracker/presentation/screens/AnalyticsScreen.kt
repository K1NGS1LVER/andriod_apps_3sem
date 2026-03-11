package com.danielpaul.financetracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.danielpaul.financetracker.presentation.components.CustomTopBar
import com.danielpaul.financetracker.presentation.components.EmptyState
import com.danielpaul.financetracker.presentation.components.ExpenseChart
import com.danielpaul.financetracker.presentation.components.SummaryCard
import com.danielpaul.financetracker.presentation.viewmodel.AnalyticsViewModel
import com.danielpaul.financetracker.theme.AccentTeal
import com.danielpaul.financetracker.theme.ExpenseRed
import com.danielpaul.financetracker.theme.IncomeGreen
import com.danielpaul.financetracker.util.CategoryUtils
import com.danielpaul.financetracker.util.CurrencyUtils.toCurrencyString
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { CustomTopBar(title = "Analytics") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    label = "Income",
                    amount = state.summary.totalIncome,
                    icon = Icons.Rounded.TrendingUp,
                    iconTint = IncomeGreen,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    label = "Expenses",
                    amount = state.summary.totalExpenses,
                    icon = Icons.Rounded.TrendingDown,
                    iconTint = ExpenseRed,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }

            // Top Category
            if (state.topCategory.isNotBlank()) {
                val catInfo = CategoryUtils.getCategoryInfo(state.topCategory)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(catInfo.color.copy(alpha = 0.1f), MaterialTheme.shapes.large)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Top Spending Category",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = state.topCategory,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = catInfo.color
                        )
                    }
                    Text(
                        text = state.summary.categoryBreakdown[state.topCategory]?.toCurrencyString() ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Monthly Bar Chart (displayed as text table for stability)
            if (state.monthlyData.isNotEmpty()) {
                Text(
                    text = "Monthly Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                state.monthlyData.forEach { monthly ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(monthly.month, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                            Text("+${monthly.income.toCurrencyString()}", color = IncomeGreen, style = MaterialTheme.typography.bodySmall)
                            Text("-${monthly.expense.toCurrencyString()}", color = ExpenseRed, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Expense Pie Chart
            Text(
                text = "Spending by Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            ExpenseChart(
                categoryBreakdown = state.summary.categoryBreakdown,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
                    .padding(16.dp)
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}
