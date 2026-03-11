package com.example.recipebook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipebook.data.model.ApodDto
import com.example.recipebook.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodDetailScreen(
    apod: ApodDto?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* empty — image is the hero */ },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = StarWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SpaceBlack.copy(alpha = 0.85f)
                )
            )
        },
        containerColor = SpaceBlack
    ) { innerPadding ->

        if (apod == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🛸",
                        fontSize = 64.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Entry not found", style = MaterialTheme.typography.titleMedium)
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // --- Hero image / video placeholder ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                if (apod.mediaType == "image") {
                    val imageUrl = apod.hdurl ?: apod.url

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = apod.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Bottom-fade gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        SpaceBlack
                                    ),
                                    startY = 150f
                                )
                            )
                    )
                } else {
                    // Video entry hero
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(listOf(SpaceNavy, SpaceBlack))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "▶",
                            fontSize = 80.sp,
                            color = NebulaPurple
                        )
                    }
                }
            }

            // --- Metadata & Explanation ---
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Date chip
                Surface(
                    shape = RoundedCornerShape(50),
                    color = SpaceNavy,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "  📅  ${apod.date}  ",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Text(
                    text = apod.title,
                    style = MaterialTheme.typography.displayLarge,
                    color = StarWhite
                )

                Spacer(Modifier.height(16.dp))

                HorizontalDivider(color = SpaceNavy)

                Spacer(Modifier.height(16.dp))

                Text(
                    text = apod.explanation,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
