package com.danielpaul.financetracker.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.danielpaul.financetracker.domain.model.Transaction
import com.danielpaul.financetracker.presentation.components.CategoryChip
import com.danielpaul.financetracker.presentation.components.CustomTopBar
import com.danielpaul.financetracker.presentation.components.EmptyState
import com.danielpaul.financetracker.presentation.components.TransactionCard
import com.danielpaul.financetracker.presentation.viewmodel.HistoryViewModel
import com.danielpaul.financetracker.theme.ExpenseRed
import com.danielpaul.financetracker.util.CategoryUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    onEditTransaction: (Transaction) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val allCategories = listOf("All") + CategoryUtils.expenseCategories + CategoryUtils.incomeCategories

    Scaffold(
        topBar = { CustomTopBar(title = "Transaction History") }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Category filter row
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allCategories) { cat ->
                    val info = if (cat == "All") null else CategoryUtils.getCategoryInfo(cat)
                    val icon = info?.icon ?: Icons.Rounded.Delete
                    val tint = info?.color ?: MaterialTheme.colorScheme.primary
                    CategoryChip(
                        label = cat,
                        icon = if (cat == "All") Icons.Rounded.Delete else icon,
                        iconTint = if (cat == "All") MaterialTheme.colorScheme.primary else tint,
                        isSelected = state.selectedCategory == cat,
                        onClick = { viewModel.setCategory(cat) }
                    )
                }
            }

            if (state.filteredTransactions.isEmpty()) {
                EmptyState(
                    message = "No transactions found",
                    subtitle = "Try adjusting your filter or add new transactions"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filteredTransactions, key = { it.id }) { transaction ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.deleteTransaction(transaction)
                                    true
                                } else false
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val bgColor by animateColorAsState(
                                    targetValue = when (dismissState.targetValue) {
                                        SwipeToDismissBoxValue.EndToStart -> ExpenseRed.copy(alpha = 0.15f)
                                        else -> Color.Transparent
                                    },
                                    label = "swipeBg"
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(bgColor, MaterialTheme.shapes.medium)
                                        .padding(end = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Rounded.Delete,
                                        contentDescription = "Delete",
                                        tint = ExpenseRed
                                    )
                                }
                            },
                            content = {
                                TransactionCard(
                                    transaction = transaction,
                                    onClick = { onEditTransaction(transaction) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
