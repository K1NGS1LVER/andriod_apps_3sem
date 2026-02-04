package com.example.revelation_2026_test.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.revelation_2026_test.R
import com.example.revelation_2026_test.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Splash screen with animated Christ University logo.
 * Auto-navigates to Welcome screen after a delay.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    // Logo scale animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Logo alpha animation
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        ),
        label = "alpha"
    )

    // Text slide-up animation
    val textOffset by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "textOffset"
    )

    // Text alpha animation
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 400
        ),
        label = "textAlpha"
    )

    // Start animation and navigate after delay
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500) // Show splash for 2.5 seconds
        onSplashComplete()
    }

    // Splash screen content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBlue,
                        MediumBlue,
                        DarkBlue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Logo
            Card(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale)
                    .alpha(alpha),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.christ_logo),
                    contentDescription = "Christ University Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Animated Title
            Text(
                text = "REVELATIONS 2026",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Subtitle
            Text(
                text = "Christ University Department Fest",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator
            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .alpha(textAlpha),
                color = AccentLightBlue,
                strokeWidth = 3.dp
            )
        }

        // Bottom branding
        Text(
            text = "© 2026 Christ University",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(textAlpha)
        )
    }
}
