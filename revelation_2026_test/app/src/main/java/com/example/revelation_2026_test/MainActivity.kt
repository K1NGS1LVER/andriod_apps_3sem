package com.example.revelation_2026_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.revelation_2026_test.navigation.AppNavigation
import com.example.revelation_2026_test.ui.theme.Revelation_2026_testTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Revelation_2026_testTheme {
                AppNavigation()
            }
        }
    }
}