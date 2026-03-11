package com.example.recipebook.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipebook.data.model.ApodDto
import com.example.recipebook.ui.theme.*

@Composable
fun ApodCard(
    apod: ApodDto,
    scrollOffset: Float = 0f,       // value in pixels from scroll position
    onClick: () -> Unit
) {
    // Subtle parallax: scale the image slightly based on scroll position
    val parallaxScale by animateFloatAsState(
        targetValue = 1f + (scrollOffset.coerceIn(-200f, 200f) / 200f) * 0.06f,
        animationSpec = tween(durationMillis = 80),
        label = "parallaxScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SpaceNavy),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // --- Image / Video Placeholder ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                if (apod.mediaType == "image") {
                    AsyncImage(
                        model = apod.url,
                        contentDescription = apod.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = parallaxScale
                                scaleY = parallaxScale
                            }
                    )

                    // Vignette overlay drawn via Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawVignette(size)
                    }
                } else {
                    // Video placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(SpaceBlack, SpaceNavy)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "▶",
                            fontSize = 48.sp,
                            color = NebulaPurple
                        )
                        Text(
                            text = "VIDEO",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }

            // --- Text Content ---
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = apod.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = NebulaBlue
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = apod.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = apod.explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/** Custom vignette — darkens edges for a cinematic look */
private fun DrawScope.drawVignette(size: androidx.compose.ui.geometry.Size) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color(0xCC000000)),
            center = Offset(size.width / 2f, size.height / 2f),
            radius = maxOf(size.width, size.height) * 0.75f
        )
    )
}
