package com.example.calculator_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var firstValue: Double = Double.NaN
    private var secondValue: Double = 0.0
    private var currentOperator: Char? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvDisplay = findViewById(R.id.tvDisplay)

        val digitButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in digitButtons) {
            findViewById<Button>(id).setOnClickListener {
                val button = it as Button
                if (tvDisplay.text.toString() == "0") {
                    tvDisplay.text = button.text
                } else {
                    tvDisplay.append(button.text)
                }
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            val currentText = tvDisplay.text.toString()
            if (!currentText.contains(".")) {
                tvDisplay.append(".")
            }
        }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            tvDisplay.text = "0"
            firstValue = Double.NaN
            currentOperator = null
        }

        findViewById<Button>(R.id.btnDel).setOnClickListener {
            val text = tvDisplay.text.toString()
            if (text.length > 1) {
                tvDisplay.text = text.substring(0, text.length - 1)
            } else {
                tvDisplay.text = "0"
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator('+') }
        findViewById<Button>(R.id.btnSub).setOnClickListener { setOperator('-') }
        findViewById<Button>(R.id.btnMul).setOnClickListener { setOperator('*') }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { setOperator('/') }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            calculate()
        }
    }

    private fun setOperator(operator: Char) {
        try {
            firstValue = tvDisplay.text.toString().toDouble()
            currentOperator = operator
            tvDisplay.text = "0"
        } catch (e: Exception) {
            tvDisplay.text = "Error"
        }
    }

    private fun calculate() {
        if (!firstValue.isNaN() && currentOperator != null) {
            try {
                secondValue = tvDisplay.text.toString().toDouble()
                val result = when (currentOperator) {
                    '+' -> firstValue + secondValue
                    '-' -> firstValue - secondValue
                    '*' -> firstValue * secondValue
                    '/' -> if (secondValue != 0.0) firstValue / secondValue else Double.NaN
                    else -> secondValue
                }
                
                if (result.isNaN()) {
                    tvDisplay.text = "Error"
                } else {
                    tvDisplay.text = if (result % 1 == 0.0) result.toLong().toString() else result.toString()
                }
                firstValue = Double.NaN
                currentOperator = null
            } catch (e: Exception) {
                tvDisplay.text = "Error"
            }
        }
    }
}
