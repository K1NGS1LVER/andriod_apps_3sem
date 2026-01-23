package com.example.jetpackedd_eg_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.jetpackedd_eg_app.ui.theme.Jetpackedd_eg_appTheme

class MainActivity : ComponentActivity() {

    private val lifecycleEvents = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLifecycleEvent("onCreate")
        enableEdgeToEdge()
        setContent {
            Jetpackedd_eg_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LifecycleLogger(lifecycleEvents, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        addLifecycleEvent("onStart")
    }

    override fun onResume() {
        super.onResume()
        addLifecycleEvent("onResume")
    }

    override fun onPause() {
        super.onPause()
        addLifecycleEvent("onPause")
    }

    override fun onStop() {
        super.onStop()
        addLifecycleEvent("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        addLifecycleEvent("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        addLifecycleEvent("onDestroy")
    }

    private fun addLifecycleEvent(name: String) {
        Log.d("MainActivity", name)
        lifecycleEvents.add(name)
    }
}

@Composable
fun LifecycleLogger(lifecycleEvents: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        lifecycleEvents.forEach {
            Text(text = it)
        }
    }
}
