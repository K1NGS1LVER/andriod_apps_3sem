package com.example.revelation_2026_test.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.revelation_2026_test.ui.theme.*
import com.example.revelation_2026_test.viewmodel.RegistrationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Registration screen with form fields and validation.
 */
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val data = viewModel.registrationData
    val scope = rememberCoroutineScope()

    // Loading state for form submission
    var isLoading by remember { mutableStateOf(false) }

    // Back confirmation dialog state
    var showBackConfirmDialog by remember { mutableStateOf(false) }

    // Track if user has interacted with each field (for showing errors)
    var nameInteracted by remember { mutableStateOf(false) }
    var mobileInteracted by remember { mutableStateOf(false) }
    var emailInteracted by remember { mutableStateOf(false) }
    var collegeInteracted by remember { mutableStateOf(false) }

    // Check if user has entered any data
    val hasEnteredData = data.name.isNotEmpty() || data.mobile.isNotEmpty() || 
                         data.email.isNotEmpty() || data.collegeName.isNotEmpty()

    // Handle back press with confirmation if data entered
    BackHandler(enabled = hasEnteredData) {
        showBackConfirmDialog = true
    }

    // Back Confirmation Dialog
    if (showBackConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBackConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningOrange,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Discard Changes?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "You have unsaved registration data. Are you sure you want to go back? Your progress will be lost.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBackConfirmDialog = false
                        viewModel.resetRegistration()
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showBackConfirmDialog = false }) {
                    Text("Keep Editing")
                }
            }
        )
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
                text = "Register for Revelations 2026",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Fill in your details to register",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar - calculates progress based on valid fields
            val progress = remember(data.name, data.mobile, data.email, data.collegeName) {
                var count = 0f
                if (viewModel.isNameValid()) count += 0.25f
                if (viewModel.isMobileValid()) count += 0.25f
                if (viewModel.isEmailValid()) count += 0.25f
                if (viewModel.isCollegeNameValid()) count += 0.25f
                count
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Form Progress",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = NeonPink,
                    trackColor = Color.White.copy(alpha = 0.3f),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Form Card
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
                    // Name Field
                    FormTextField(
                        value = data.name,
                        onValueChange = {
                            viewModel.updateName(it)
                            nameInteracted = true
                        },
                        label = "Full Name",
                        icon = Icons.Default.Person,
                        isError = nameInteracted && !viewModel.isNameValid(),
                        errorMessage = "Name must be at least 3 characters",
                        keyboardType = KeyboardType.Text
                    )

                    // Mobile Field
                    FormTextField(
                        value = data.mobile,
                        onValueChange = {
                            viewModel.updateMobile(it)
                            mobileInteracted = true
                        },
                        label = "Mobile Number",
                        icon = Icons.Default.Phone,
                        isError = mobileInteracted && !viewModel.isMobileValid(),
                        errorMessage = "Enter a valid 10-digit mobile number",
                        keyboardType = KeyboardType.Phone
                    )

                    // Email Field
                    FormTextField(
                        value = data.email,
                        onValueChange = {
                            viewModel.updateEmail(it)
                            emailInteracted = true
                        },
                        label = "Email Address",
                        icon = Icons.Default.Email,
                        isError = emailInteracted && !viewModel.isEmailValid(),
                        errorMessage = "Enter a valid email address",
                        keyboardType = KeyboardType.Email
                    )

                    // College Field
                    FormTextField(
                        value = data.collegeName,
                        onValueChange = {
                            viewModel.updateCollegeName(it)
                            collegeInteracted = true
                        },
                        label = "College Name",
                        icon = Icons.Default.Home,
                        isError = collegeInteracted && !viewModel.isCollegeNameValid(),
                        errorMessage = "College name is required",
                        keyboardType = KeyboardType.Text
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Next Button with Loading Indicator
            Button(
                onClick = {
                    if (!isLoading) {
                        if (viewModel.isRegistrationFormValid()) {
                            isLoading = true
                            scope.launch {
                                delay(1500) // Simulate form processing
                                isLoading = false
                                Toast.makeText(context, "Details saved!", Toast.LENGTH_SHORT).show()
                                onNextClick()
                            }
                        } else {
                            // Mark all fields as interacted to show errors
                            nameInteracted = true
                            mobileInteracted = true
                            emailInteracted = true
                            collegeInteracted = true
                            Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPink
                ),
                enabled = !isLoading,
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Saving...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "Next",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Reusable form text field with icon and error handling.
 */
@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isError: Boolean,
    errorMessage: String,
    keyboardType: KeyboardType
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isError) ErrorRed else MidnightPurple
                )
            },
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MidnightPurple,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                errorBorderColor = ErrorRed,
                focusedLabelColor = MidnightPurple,
                cursorColor = MidnightPurple
            )
        )

        // Error message
        if (isError) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
