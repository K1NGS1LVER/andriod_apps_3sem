package com.example.revelation_2026_test.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.revelation_2026_test.ui.theme.*
import com.example.revelation_2026_test.viewmodel.RegistrationViewModel

/**
 * Event selection screen with dropdown for group and number picker for events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSelectionScreen(
    viewModel: RegistrationViewModel,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val data = viewModel.registrationData

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }

    // Available event groups
    val eventGroups = listOf(
        "Technical Events",
        "Cultural Events",
        "Sports Events",
        "Literary Events",
        "Art & Craft"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepPurple, ElectricBlue.copy(alpha = 0.8f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Back button row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Select Your Events",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "Choose your preferred event category and number of events",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Group Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Event Group",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MidnightPurple
                    )

                    // Dropdown Menu
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = data.groupName.ifEmpty { "Select a group" },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MidnightPurple,
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            eventGroups.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group) },
                                    onClick = {
                                        viewModel.updateGroupName(group)
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Event Counter Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Number of Events",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MidnightPurple
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Decrement button
                        FilledIconButton(
                            onClick = {
                                if (data.numberOfEvents > 1) {
                                    viewModel.updateNumberOfEvents(data.numberOfEvents - 1)
                                }
                            },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MidnightPurple,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                        }

                        // Count display
                        Text(
                            text = "${data.numberOfEvents}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPink,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )

                        // Increment button
                        FilledIconButton(
                            onClick = {
                                if (data.numberOfEvents < 5) {
                                    viewModel.updateNumberOfEvents(data.numberOfEvents + 1)
                                }
                            },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MidnightPurple,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }

                    Text(
                        text = "You can register for 1-5 events",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = {
                    if (viewModel.isEventSelectionValid()) {
                        onContinueClick()
                    } else {
                        Toast.makeText(context, "Please select an event group", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
