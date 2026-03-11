package com.danielpaul.financetracker.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danielpaul.financetracker.theme.CategoryBills
import com.danielpaul.financetracker.theme.CategoryEntertainment
import com.danielpaul.financetracker.theme.CategoryFood
import com.danielpaul.financetracker.theme.CategoryTransport
import com.danielpaul.financetracker.theme.CategoryShopping
import com.danielpaul.financetracker.theme.CategoryHealth
import com.danielpaul.financetracker.theme.CategoryOther
import com.danielpaul.financetracker.util.CurrencyUtils.toCurrencyString

private val pieColors = listOf(
    CategoryFood, CategoryTransport, CategoryShopping,
    CategoryEntertainment, CategoryHealth, CategoryBills, CategoryOther
)

@Composable
fun ExpenseChart(
    categoryBreakdown: Map<String, Double>,
    modifier: Modifier = Modifier,
    chartSize: Dp = 180.dp,
    strokeWidth: Dp = 28.dp
) {
    val total = categoryBreakdown.values.sum()
    
    if (categoryBreakdown.isEmpty() || total <= 0.0) {
        EmptyState(
            message = "No expense data yet",
            subtitle = "Add transactions to see your spending breakdown",
            modifier = modifier
        )
        return
    }

    var animTriggered by remember { mutableStateOf(false) }
    val animSweep by animateFloatAsState(
        targetValue = if (animTriggered) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "pieSweep"
    )
    LaunchedEffect(Unit) { animTriggered = true }

    val entries = categoryBreakdown.entries.toList()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(chartSize)) {
                var startAngle = -90f
                entries.forEachIndexed { idx, (_, value) ->
                    val sweepAngle = ((value / total) * 360f * animSweep).toFloat()
                    drawArc(
                        color = pieColors[idx % pieColors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                        topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                        size = Size(
                            size.width - strokeWidth.toPx(),
                            size.height - strokeWidth.toPx()
                        )
                    )
                    startAngle += sweepAngle
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = total.toCurrencyString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            entries.forEachIndexed { idx, (label, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(pieColors[idx % pieColors.size])
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${((amount / total) * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = amount.toCurrencyString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
