package com.example.calcii

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var resultTV: TextView
    private lateinit var workingsTV: TextView

    private var operand1: Double = 0.0
    private var operand2: Double = 0.0
    private var operator: String = ""
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTV = findViewById(R.id.resultTV)
        workingsTV = findViewById(R.id.workingsTV)

        val numberButtons = listOf<Button>(
            findViewById(R.id.zero), findViewById(R.id.one), findViewById(R.id.two),
            findViewById(R.id.three), findViewById(R.id.four), findViewById(R.id.five),
            findViewById(R.id.six), findViewById(R.id.seven), findViewById(R.id.eight),
            findViewById(R.id.nine), findViewById(R.id.decimal)
        )

        val operatorButtons = listOf<Button>(
            findViewById(R.id.plus), findViewById(R.id.minus), findViewById(R.id.multiply),
            findViewById(R.id.divide)
        )

        numberButtons.forEach { button ->
            button.setOnClickListener { onNumberClick(it) }
        }

        operatorButtons.forEach { button ->
            button.setOnClickListener { onOperatorClick(it) }
        }

        findViewById<Button>(R.id.ac).setOnClickListener { onClearClick() }
        findViewById<Button>(R.id.equals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.plusMinus).setOnClickListener { onPlusMinusClick() }
        findViewById<Button>(R.id.percent).setOnClickListener { onPercentClick() }
    }

    private fun onNumberClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        if (isNewOperation) {
            resultTV.text = buttonText
            isNewOperation = false
        } else {
            val currentText = resultTV.text.toString()
            if (buttonText == "." && currentText.contains(".")) {
                return
            }
            resultTV.text = currentText + buttonText
        }
        workingsTV.text = workingsTV.text.toString() + buttonText
    }

    private fun onOperatorClick(view: View) {
        val button = view as Button
        val newOperator = button.text.toString()

        if (operator.isNotEmpty() && !isNewOperation) {
            operand2 = resultTV.text.toString().toDouble()
            operand1 = calculate(operand1, operand2, operator)
            resultTV.text = formatResult(operand1)
        } else {
            operand1 = resultTV.text.toString().toDouble()
        }

        operator = newOperator
        isNewOperation = true
        workingsTV.text = workingsTV.text.toString() + " $newOperator "
    }

    private fun onEqualsClick() {
        if (operator.isEmpty()) return

        operand2 = resultTV.text.toString().toDouble()
        val result = calculate(operand1, operand2, operator)

        resultTV.text = formatResult(result)
        operand1 = result
        operator = ""
        isNewOperation = true
        workingsTV.text = ""
    }

    private fun calculate(op1: Double, op2: Double, op: String): Double {
        return when (op) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "×" -> op1 * op2
            "÷" -> if (op2 != 0.0) op1 / op2 else 0.0
            else -> 0.0
        }
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }

    private fun onClearClick() {
        resultTV.text = "0"
        workingsTV.text = ""
        isNewOperation = true
        operand1 = 0.0
        operand2 = 0.0
        operator = ""
    }

    private fun onPlusMinusClick() {
        val currentValue = resultTV.text.toString().toDouble()
        resultTV.text = formatResult(-currentValue)
    }

    private fun onPercentClick() {
        val currentValue = resultTV.text.toString().toDouble()
        resultTV.text = formatResult(currentValue / 100)
        workingsTV.text = ""
    }
}
