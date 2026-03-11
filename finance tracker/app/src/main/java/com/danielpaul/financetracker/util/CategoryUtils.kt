package com.danielpaul.financetracker.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.WorkOutline
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.danielpaul.financetracker.theme.CategoryBills
import com.danielpaul.financetracker.theme.CategoryEntertainment
import com.danielpaul.financetracker.theme.CategoryFood
import com.danielpaul.financetracker.theme.CategoryHealth
import com.danielpaul.financetracker.theme.CategoryOther
import com.danielpaul.financetracker.theme.CategorySalary
import com.danielpaul.financetracker.theme.CategoryShopping
import com.danielpaul.financetracker.theme.CategoryTransport
import com.danielpaul.financetracker.theme.IncomeGreen

data class CategoryInfo(val icon: ImageVector, val color: Color, val label: String)

object CategoryUtils {

    val expenseCategories = listOf(
        "Food", "Transport", "Shopping", "Entertainment",
        "Health", "Bills", "Housing", "Other"
    )

    val incomeCategories = listOf(
        "Salary", "Freelance", "Investment", "Other"
    )

    fun getCategoryInfo(category: String): CategoryInfo = when (category) {
        "Food" -> CategoryInfo(Icons.Rounded.Fastfood, CategoryFood, "Food")
        "Transport" -> CategoryInfo(Icons.Rounded.DirectionsBus, CategoryTransport, "Transport")
        "Shopping" -> CategoryInfo(Icons.Rounded.ShoppingBag, CategoryShopping, "Shopping")
        "Entertainment" -> CategoryInfo(Icons.Rounded.Movie, CategoryEntertainment, "Entertainment")
        "Health" -> CategoryInfo(Icons.Rounded.LocalHospital, CategoryHealth, "Health")
        "Bills" -> CategoryInfo(Icons.Rounded.ReceiptLong, CategoryBills, "Bills")
        "Housing" -> CategoryInfo(Icons.Rounded.Home, CategoryBills, "Housing")
        "Salary" -> CategoryInfo(Icons.Rounded.WorkOutline, CategorySalary, "Salary")
        "Freelance" -> CategoryInfo(Icons.Rounded.AccountBalanceWallet, IncomeGreen, "Freelance")
        "Investment" -> CategoryInfo(Icons.Rounded.FitnessCenter, IncomeGreen, "Investment")
        else -> CategoryInfo(Icons.Rounded.ReceiptLong, CategoryOther, "Other")
    }
}
