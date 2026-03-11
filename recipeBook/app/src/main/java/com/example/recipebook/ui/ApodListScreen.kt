package com.example.recipebook.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.recipebook.data.model.ApodDto
import com.example.recipebook.ui.components.ApodCard
import com.example.recipebook.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodListScreen(
    uiState: ApodUiState,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onCardClick: (ApodDto) -> Unit,
    onRetry: () -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                // --- App title ---
                Text(
                    text = "🌌  NASA APOD",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp, top = 14.dp, bottom = 8.dp)
                )

                // --- Search bar ---
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "Search by title, date, or keyword…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CosmicGrey
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = NebulaBlue
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear search",
                                    tint = CosmicGrey
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = NebulaBlue,
                        unfocusedBorderColor = SpaceNavy,
                        focusedContainerColor   = SpaceNavy,
                        unfocusedContainerColor = SpaceNavy,
                        cursorColor          = NebulaBlue,
                        focusedTextColor     = StarWhite,
                        unfocusedTextColor   = StarWhite
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = StarWhite),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        },
        containerColor = SpaceBlack
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is ApodUiState.Loading -> {
                    CircularProgressIndicator(
                        color = NebulaBlue,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ApodUiState.Success -> {
                    val apods = uiState.apods

                    if (apods.isEmpty()) {
                        // No results for the query
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔭", style = MaterialTheme.typography.displayLarge)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "No results found for \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CosmicGrey,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(apods) { index, apod ->
                                val itemInfo = listState.layoutInfo.visibleItemsInfo
                                    .firstOrNull { it.index == index }
                                val scrollOffset = itemInfo?.offset?.toFloat() ?: 0f

                                ApodCard(
                                    apod = apod,
                                    scrollOffset = scrollOffset,
                                    onClick = { onCardClick(apod) }
                                )
                            }
                        }
                    }
                }

                is ApodUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "🛸 Lost in space!",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = uiState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = ErrorRed
                        )
                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NebulaBlue,
                                contentColor = SpaceBlack
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}
