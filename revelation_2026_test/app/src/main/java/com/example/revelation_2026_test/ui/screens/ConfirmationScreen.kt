package com.example.revelation_2026_test.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.revelation_2026_test.ui.theme.*
import com.example.revelation_2026_test.viewmodel.RegistrationViewModel
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Confirmation screen displaying all registration details with edit and confirm options.
 */
@Composable
fun ConfirmationScreen(
    viewModel: RegistrationViewModel,
    onEditClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    val context = LocalContext.current
    val data = viewModel.registrationData
    var showSuccessDialog by remember { mutableStateOf(false) }
    var confirmationId by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Share registration function
    fun shareRegistration() {
        val shareText = """
            |🎉 REVELATIONS 2026 REGISTRATION
            |================================
            |
            |📝 Personal Details:
            |• Name: ${data.name}
            |• Mobile: ${data.mobile}
            |• Email: ${data.email}
            |• College: ${data.collegeName}
            |
            |🎯 Event Details:
            |• Group: ${data.groupName}
            |• Events: ${data.numberOfEvents}
            |
            |✅ Status: Confirmed
            |
            |#Revelations2026 #ChristUniversity
        """.trimMargin()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "My Revelations 2026 Registration")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share Registration"))
    }

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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Confirm Your Registration",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Please review your details before confirming",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Personal Details Card
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = NeonPink,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Personal Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightPurple
                        )
                    }

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))

                    DetailRow(icon = Icons.Default.Person, label = "Name", value = data.name)
                    DetailRow(icon = Icons.Default.Phone, label = "Mobile", value = data.mobile)
                    DetailRow(icon = Icons.Default.Email, label = "Email", value = data.email)
                    DetailRow(icon = Icons.Default.Home, label = "College", value = data.collegeName)
                }
            }

            // Event Details Card
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = NeonPink,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Event Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightPurple
                        )
                    }

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))

                    DetailRow(icon = Icons.Default.List, label = "Group", value = data.groupName)
                    DetailRow(icon = Icons.Default.Star, label = "Number of Events", value = data.numberOfEvents.toString())
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Edit Button
                OutlinedButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Confirm Button with Loading Indicator
                Button(
                    onClick = {
                        if (!isLoading) {
                            isLoading = true
                            scope.launch {
                                delay(2000) // Simulate registration processing
                                isLoading = false
                                confirmationId = generateConfirmationId()
                                showSuccessDialog = true
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    enabled = !isLoading,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Processing...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Confirm",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Share Button
            OutlinedButton(
                onClick = { shareRegistration() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Share Registration",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Success Dialog
        if (showSuccessDialog) {
            SuccessDialog(
                confirmationId = confirmationId,
                onDismiss = {
                    showSuccessDialog = false
                    viewModel.resetRegistration()
                    onDoneClick()
                }
            )
        }
    }
}

/**
 * Detail row showing label and value with icon.
 */
@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

/**
 * Success dialog shown after confirmation.
 */
@Composable
private fun SuccessDialog(
    confirmationId: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Success checkmark
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(40.dp),
                    color = SuccessGreen.copy(alpha = 0.15f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            modifier = Modifier.size(56.dp),
                            tint = SuccessGreen
                        )
                    }
                }

                Text(
                    text = "Registration Successful!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MidnightPurple,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Thank you for registering for Revelations 2026!",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                // Confirmation ID
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MidnightPurple.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Confirmation ID",
                            fontSize = 12.sp,
                            color = MidnightPurple
                        )
                        Text(
                            text = confirmationId,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonPink
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MidnightPurple)
                ) {
                    Text(
                        text = "Done",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Generate a random 6-digit confirmation ID.
 */
private fun generateConfirmationId(): String {
    return "REV-${Random.nextInt(100000, 999999)}"
}
