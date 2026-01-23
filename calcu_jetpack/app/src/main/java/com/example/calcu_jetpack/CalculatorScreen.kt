package com.example.calcu_jetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen() {
    val viewModel = viewModel<CalculatorViewModel>()
    val state by viewModel.state.collectAsState()
    val lightGray = Color(0xFFA5A5A5)
    val orange = Color(0xFFF1A33B)
    val darkGray = Color(0xFF333333)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                reverseLayout = true
            ) {
                items(state.history) {
                    Text(
                        text = it,
                        color = Color.Gray,
                        fontSize = 24.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Text(
                text = state.display,
                color = Color.White,
                fontSize = 80.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                maxLines = 1
            )

            val buttonRows = listOf(
                listOf("AC", "+/-", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
            )

            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { buttonText ->
                        val backgroundColor = when (buttonText) {
                            "AC", "+/-", "%" -> lightGray
                            "÷", "×", "-", "+" -> orange
                            else -> darkGray
                        }
                        val textColor = if (buttonText in listOf("AC", "+/-", "%")) Color.Black else Color.White

                        CalculatorButton(
                            text = buttonText,
                            modifier = Modifier.weight(1f),
                            backgroundColor = backgroundColor,
                            textColor = textColor
                        ) {
                            val action = when(buttonText) {
                                "AC" -> CalculatorAction.Clear
                                "+/-" -> CalculatorAction.ToggleSign
                                "%" -> CalculatorAction.Percentage
                                "÷" -> CalculatorAction.Operation(CalculatorOperation.Divide)
                                "×" -> CalculatorAction.Operation(CalculatorOperation.Multiply)
                                "-" -> CalculatorAction.Operation(CalculatorOperation.Subtract)
                                "+" -> CalculatorAction.Operation(CalculatorOperation.Add)
                                else -> CalculatorAction.Number(buttonText.toInt())
                            }
                            viewModel.onAction(action)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(2f),
                    backgroundColor = darkGray
                ) { viewModel.onAction(CalculatorAction.Number(0)) }
                CalculatorButton(
                    text = ".",
                    modifier = Modifier.weight(1f),
                    backgroundColor = darkGray
                ) { viewModel.onAction(CalculatorAction.Decimal) }
                CalculatorButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    backgroundColor = orange
                ) { viewModel.onAction(CalculatorAction.Calculate) }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .then(modifier)
            .aspectRatio(if (text == "0") 2f else 1f)
    ) {
        Text(
            text = text,
            fontSize = 36.sp,
            color = textColor
        )
    }
}