package com.example.counter_app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Countdown Screen ─────────────────────────────────────────────────────────

/**
 * Full-screen countdown timer with:
 *  – Set target using +/− buttons (hidden while running)
 *  – Animated circular progress ring (primary → green running → red finished)
 *  – Animated remaining-seconds display inside the ring
 *  – Start / Pause + Reset controls
 *  – "Time's Up! 🎉" banner + haptic on completion
 */
@Composable
fun CountdownScreen(
    viewModel: CounterViewModel,
    onHaptic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val target    by viewModel.countdownTarget
    val remaining by viewModel.countdownRemaining
    val isRunning by viewModel.isCountdownRunning
    val finished  by viewModel.countdownFinished

    // Haptic pulse when countdown hits 0
    LaunchedEffect(finished) {
        if (finished) onHaptic()
    }

    val progress = if (target > 0) remaining.toFloat() / target.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "countdownProgress"
    )

    // Ring colour: primary (idle) → green (running) → red (finished)
    val ringColor = when {
        finished  -> Color(0xFFD50000)
        isRunning -> Color(0xFF00C853)
        else      -> MaterialTheme.colorScheme.primary
    }
    val animatedRingColor by animateColorAsState(
        targetValue = ringColor,
        animationSpec = tween(400),
        label = "ringColor"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ── Target setter (hidden while running / finished) ───────────────────
        AnimatedVisibility(visible = !isRunning && !finished) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Set target (seconds)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RapidActionButton(
                        label = "−",
                        onAction = { viewModel.setCountdownTarget(target - 1) },
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "$target",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    RapidActionButton(
                        label = "+",
                        onAction = { viewModel.setCountdownTarget(target + 1) },
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // ── Circular progress ring ────────────────────────────────────────────
        Box(contentAlignment = Alignment.Center) {
            // Track ring (always full)
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(220.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 12.dp
            )
            // Progress ring
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(220.dp),
                color = animatedRingColor,
                strokeWidth = 12.dp
            )
            // Centre: animated remaining time
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedContent(
                    targetState = remaining,
                    transitionSpec = {
                        (slideInVertically { h -> -h } + fadeIn(tween(200)))
                            .togetherWith(slideOutVertically { h -> h } + fadeOut(tween(200)))
                            .using(SizeTransform(clip = false))
                    },
                    label = "remainingAnim"
                ) { r ->
                    Text(
                        text = "$r",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp
                        ),
                        color = animatedRingColor
                    )
                }
                Text(
                    text = "sec",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Finished banner ───────────────────────────────────────────────────
        AnimatedVisibility(visible = finished) {
            Text(
                text = "Time's Up! \uD83C\uDF89",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFD50000),
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // ── Control buttons ───────────────────────────────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset button
            IconButton(
                onClick = { viewModel.resetCountdown() },
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Reset countdown",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Start / Pause button
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = if (isRunning) MaterialTheme.colorScheme.secondary
                                else MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = {
                            if (isRunning) viewModel.pauseCountdown()
                            else viewModel.startCountdown()
                            tryAwaitRelease()
                        })
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
