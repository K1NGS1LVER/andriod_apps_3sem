package com.example.counter_app

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.counter_app.ui.theme.Counter_appTheme
import kotlinx.coroutines.delay

// ─── Constants ───────────────────────────────────────────────────────────────

private const val SPLASH_DURATION_MS = 2000L
private const val FADE_DURATION_MS = 400
private const val COUNTER_ANIM_DURATION_MS = 300
private const val SCALE_PRESS = 0.92f
private const val SCALE_DEFAULT = 1f
private const val IMAGE_SIZE_DP = 200
private const val BUTTON_SIZE_DP = 64
private const val CARD_CORNER_DP = 24
private const val SPLASH_LOGO_SIZE_DP = 140
private const val RAPID_DELAY_START_MS = 400L
private const val RAPID_DELAY_MIN_MS = 80L

// ─── Activity ─────────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var counterViewModel: CounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        counterViewModel = ViewModelProvider(this)[CounterViewModel::class.java]
        shakeDetector = ShakeDetector {
            counterViewModel.increment()
            triggerHaptic()
        }

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            Counter_appTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    counterViewModel = counterViewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    onHaptic = ::triggerHaptic
                )
            }
        }
    }

    private fun triggerHaptic() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Vibrator::class.java)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(60)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(shakeDetector, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }
}

// ─── Navigation ───────────────────────────────────────────────────────────────

@Composable
fun AppNavigation(
    counterViewModel: CounterViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onHaptic: () -> Unit
) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else {
        CounterApp(
            viewModel = counterViewModel,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onHaptic = onHaptic
        )
    }
}

// ─── Splash Screen ────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(SPLASH_DURATION_MS)
        visible = false
        delay(FADE_DURATION_MS.toLong())
        onTimeout()
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = FADE_DURATION_MS),
        label = "splashAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.christ_logo),
                contentDescription = "Christ University Logo",
                modifier = Modifier.size(SPLASH_LOGO_SIZE_DP.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Counter",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Daniel Paul",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Reg No: 2547159",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

// ─── App Mode ─────────────────────────────────────────────────────────────────

private enum class AppMode { COUNTER, COUNTDOWN }

// ─── Counter App ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterApp(
    viewModel: CounterViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onHaptic: () -> Unit
) {
    var appMode by remember { mutableStateOf(AppMode.COUNTER) }
    val count by viewModel.count
    val lastAction by viewModel.lastAction
    val stepSize by viewModel.stepSize
    val context = LocalContext.current

    var showResetDialog by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }

    // ── Background image ──────────────────────────────────────────────────────
    val bgImageRes = when (lastAction) {
        LastAction.INCREMENT -> R.drawable.inc
        LastAction.DECREMENT -> R.drawable.dec
        null -> R.drawable.grass
    }

    // ── Color tint overlay ────────────────────────────────────────────────────
    val tintColor = when (lastAction) {
        LastAction.INCREMENT -> Color(0xFF00C853)  // vivid green
        LastAction.DECREMENT -> Color(0xFFD50000)  // vivid red
        null -> Color.Transparent
    }
    val tintAlpha by animateFloatAsState(
        targetValue = if (lastAction != null) 0.18f else 0f,
        animationSpec = tween(durationMillis = FADE_DURATION_MS),
        label = "tintAlpha"
    )

    // ── Reset confirmation dialog ─────────────────────────────────────────────
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Counter") },
            text = { Text("Are you sure you want to reset the counter to 0?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.reset()
                    showResetDialog = false
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Base background colour
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))

        // Animated background image
        AnimatedContent(
            targetState = bgImageRes,
            transitionSpec = {
                fadeIn(animationSpec = tween(FADE_DURATION_MS)) togetherWith
                        fadeOut(animationSpec = tween(FADE_DURATION_MS)) using
                        SizeTransform(clip = false)
            },
            label = "bgTransition"
        ) { targetBg ->
            Image(
                painter = painterResource(id = targetBg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.35f),
                contentScale = ContentScale.Crop
            )
        }

        // Color tint overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(tintColor.copy(alpha = tintAlpha))
        )

        // Main scaffold (transparent)
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        TabRow(
                            selectedTabIndex = appMode.ordinal,
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.width(220.dp)
                        ) {
                            Tab(
                                selected = appMode == AppMode.COUNTER,
                                onClick = { appMode = AppMode.COUNTER },
                                text = { Text("Counter", style = MaterialTheme.typography.labelLarge) }
                            )
                            Tab(
                                selected = appMode == AppMode.COUNTDOWN,
                                onClick = { appMode = AppMode.COUNTDOWN },
                                text = { Text("Countdown", style = MaterialTheme.typography.labelLarge) }
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "My counter is at $count! 🎯")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share count"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                        IconButton(onClick = { showHistory = !showHistory }) {
                            Icon(Icons.Default.History, contentDescription = "History")
                        }
                        IconButton(onClick = { showResetDialog = true }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset")
                        }
                        IconButton(onClick = onToggleTheme) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle theme"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            if (appMode == AppMode.COUNTER) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedCounterImage(lastAction = lastAction)
                    Spacer(modifier = Modifier.height(32.dp))
                    CounterDisplay(count = count)
                    Spacer(modifier = Modifier.height(24.dp))
                    StepSizeSelector(currentStep = stepSize, onStepSelected = viewModel::setStepSize)
                    Spacer(modifier = Modifier.height(32.dp))
                    ActionButtonRow(
                        onIncrement = { viewModel.increment(); onHaptic() },
                        onDecrement = { viewModel.decrement(); onHaptic() }
                    )
                    if (showHistory && viewModel.history.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        HistoryPanel(history = viewModel.history)
                    }
                }
            } else {
                CountdownScreen(
                    viewModel = viewModel,
                    onHaptic = onHaptic,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

// ─── Step Size Selector ───────────────────────────────────────────────────────

@Composable
fun StepSizeSelector(currentStep: Int, onStepSelected: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Step:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        listOf(1, 5, 10).forEach { step ->
            val selected = step == currentStep
            Surface(
                onClick = { onStepSelected(step) },
                shape = RoundedCornerShape(50),
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                modifier = Modifier.size(width = 52.dp, height = 36.dp),
                tonalElevation = if (selected) 0.dp else 2.dp
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "$step",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = if (selected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// ─── History Panel ────────────────────────────────────────────────────────────

@Composable
fun HistoryPanel(history: List<HistoryEntry>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Recent Actions",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(modifier = Modifier.height(160.dp)) {
                items(history) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entry.label,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = when {
                                entry.label.startsWith("+") -> Color(0xFF00C853)
                                entry.label.startsWith("-") -> Color(0xFFD50000)
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                        Text(
                            text = entry.timestamp,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// ─── Animated Counter Image ───────────────────────────────────────────────────

@Composable
fun AnimatedCounterImage(lastAction: LastAction?) {
    val imageRes = when (lastAction) {
        LastAction.INCREMENT -> R.drawable.increment_image
        LastAction.DECREMENT -> R.drawable.decrement_image
        null -> null
    }

    Card(
        modifier = Modifier.size(IMAGE_SIZE_DP.dp),
        shape = RoundedCornerShape(CARD_CORNER_DP.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (imageRes != null) {
                AnimatedContent(
                    targetState = imageRes,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(FADE_DURATION_MS)) togetherWith
                                fadeOut(animationSpec = tween(FADE_DURATION_MS)) using
                                SizeTransform(clip = false)
                    },
                    label = "imageTransition"
                ) { targetImage ->
                    Image(
                        painter = painterResource(id = targetImage),
                        contentDescription = "Counter action image",
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                Text(
                    text = "Tap a button\nto begin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ─── Counter Display ──────────────────────────────────────────────────────────

@Composable
fun CounterDisplay(count: Int) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically { h -> h } + fadeIn(tween(COUNTER_ANIM_DURATION_MS)))
                    .togetherWith(slideOutVertically { h -> -h } + fadeOut(tween(COUNTER_ANIM_DURATION_MS)))
            } else {
                (slideInVertically { h -> -h } + fadeIn(tween(COUNTER_ANIM_DURATION_MS)))
                    .togetherWith(slideOutVertically { h -> h } + fadeOut(tween(COUNTER_ANIM_DURATION_MS)))
            } using SizeTransform(clip = false)
        },
        label = "counterAnimation"
    ) { targetCount ->
        Text(
            text = "$targetCount",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 72.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// ─── Action Buttons ───────────────────────────────────────────────────────────

@Composable
fun ActionButtonRow(onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RapidActionButton(
            label = "−",
            onAction = onDecrement,
            containerColor = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(32.dp))
        RapidActionButton(
            label = "+",
            onAction = onIncrement,
            containerColor = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * A button that fires [onAction] immediately on tap, and then fires repeatedly
 * with accelerating speed when held down (long-press rapid count).
 */
@Composable
fun RapidActionButton(
    label: String,
    onAction: () -> Unit,
    containerColor: Color
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) SCALE_PRESS else SCALE_DEFAULT,
        animationSpec = tween(durationMillis = 80),
        label = "buttonScale"
    )

    // Rapid-fire loop while held
    LaunchedEffect(pressed) {
        if (!pressed) return@LaunchedEffect
        var delay = RAPID_DELAY_START_MS
        delay(delay)                       // initial hold threshold
        while (pressed) {
            onAction()
            delay(delay)
            if (delay > RAPID_DELAY_MIN_MS) delay = (delay * 0.85).toLong()
        }
    }

