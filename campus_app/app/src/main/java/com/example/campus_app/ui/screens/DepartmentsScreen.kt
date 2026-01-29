package com.example.campus_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.campus_app.R
import com.example.campus_app.data.Department
import com.example.campus_app.ui.components.Footer

@Composable
fun DepartmentsScreen(onNavigateToEventDetails: () -> Unit) {
    val departments = listOf(
        Department("Computer Science", R.drawable.computer_science_department),
        Department("Business Administration", R.drawable.bba_department),
        Department("Psychology", R.drawable.psychology_department)
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(departments) { department ->
            DepartmentCard(department, onNavigateToEventDetails)
        }
        item {
            Footer()
        }
    }
}

@Composable
fun DepartmentCard(department: Department, onNavigateToEventDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onNavigateToEventDetails() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = department.imageId),
                contentDescription = department.name,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = department.name, style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}