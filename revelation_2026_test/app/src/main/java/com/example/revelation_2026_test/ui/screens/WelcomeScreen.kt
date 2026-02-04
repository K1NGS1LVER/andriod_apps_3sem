package com.example.revelation_2026_test.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.revelation_2026_test.R
import com.example.revelation_2026_test.ui.theme.DeepPurple
import com.example.revelation_2026_test.ui.theme.ElectricBlue
import com.example.revelation_2026_test.ui.theme.NeonPink
import kotlinx.coroutines.delay

/**
 * Welcome screen with university logo, department image carousel, and Get Started button.
 */
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit
) {
    // Animation visibility state
    var isVisible by remember { mutableStateOf(false) }

    // Department images for carousel
    val departmentImages = listOf(
        R.drawable.computer_science_department,
        R.drawable.bba_department,
        R.drawable.psychology_department,
        R.drawable.tech_fest,
        R.drawable.sports_day
    )

    // Pager state for image carousel
    val pagerState = rememberPagerState(pageCount = { departmentImages.size })

    // Auto-scroll the carousel
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000) // 3 seconds per image
            val nextPage = (pagerState.currentPage + 1) % departmentImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    // Trigger animation on composition
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepPurple,
                        ElectricBlue,
                        DeepPurple.copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(800)
                        )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // University Logo
                    Card(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.christ_logo),
                            contentDescription = "Christ University Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // App Title
                    Text(
                        text = "Revelations 2026",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    // Subtitle
                    Text(
                        text = "Department Fest Registration",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Department Image Carousel
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1200))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Our Departments",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    // Image Pager/Carousel
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            Image(
                                painter = painterResource(id = departmentImages[page]),
                                contentDescription = "Department Image ${page + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Page Indicators
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(departmentImages.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index) NeonPink
                                        else Color.White.copy(alpha = 0.5f)
                                    )
                            )
                        }
                    }
                }
            }

            // Get Started Button
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1400)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(1000)
                        )
            ) {
                Button(
                    onClick = onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonPink
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
