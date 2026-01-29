package com.example.campus_app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.campus_app.R
import com.example.campus_app.data.Event
import com.example.campus_app.ui.components.Footer

@Composable
fun EventDetailsScreen() {
    val events = listOf(
        Event("Tech Fest 2024", "25th July 2024", "10:00 AM - 5:00 PM", "Main Auditorium", R.drawable.tech_fest),
        Event("Annual Sports Day", "10th August 2024", "9:00 AM - 4:00 PM", "University Grounds", R.drawable.sports_day),
        Event("Cultural Night", "20th September 2024", "6:00 PM - 10:00 PM", "Open Air Theatre", R.drawable.tech_fest) // Placeholder
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(events) { event ->
            EventCard(event)
        }
        item {
            Footer()
        }
    }
}

@Composable
fun EventCard(event: Event) {
    var expanded by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (expanded) 1.0f else 0.95f, label = "card-scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .scale(scale)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = event.imageId),
                contentDescription = event.title,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = event.title, style = MaterialTheme.typography.headlineSmall)
                AnimatedVisibility(visible = expanded) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Date: ${event.date}")
                        Text(text = "Time: ${event.time}")
                        Text(text = "Venue: ${event.venue}")
                    }
                }
            }
        }
    }
}