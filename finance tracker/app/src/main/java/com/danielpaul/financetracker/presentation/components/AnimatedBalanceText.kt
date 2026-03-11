package com.danielpaul.financetracker.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.danielpaul.financetracker.util.CurrencyUtils.toCurrencyString

@Composable
fun AnimatedBalanceText(
    amount: Double,
    style: TextStyle = MaterialTheme.typography.displayMedium,
    color: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    var triggered by remember { mutableStateOf(false) }
    val animatedAmount by animateFloatAsState(
        targetValue = if (triggered) amount.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "balance"
    )

    LaunchedEffect(amount) { triggered = true }

    Text(
        text = animatedAmount.toDouble().toCurrencyString(),
        style = style,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
    )
}
