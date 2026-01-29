package com.example.campus_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campus_app.R
import com.example.campus_app.data.UpcomingEvent
import com.example.campus_app.navigation.Screen
import com.example.campus_app.ui.components.Footer

@Composable
fun HomeScreen(navController: NavController) {
    val upcomingEvents = listOf(
        UpcomingEvent("Tech Fest 2024", "25th July", R.drawable.tech_fest),
        UpcomingEvent("Sports Day", "10th August", R.drawable.sports_day),
        UpcomingEvent("Cultural Night", "20th September", R.drawable.tech_fest) // Placeholder
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.christ_logo),
            contentDescription = "Christ University Logo",
            modifier = Modifier.height(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Welcome to Campus Connect!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Upcoming Events", style = MaterialTheme.typography.titleLarge)
        LazyRow(modifier = Modifier.padding(top = 8.dp)) {
            items(upcomingEvents) { event ->
                EventCard(event, navController)
            }
        }
        Footer()
    }
}

@Composable
fun EventCard(event: UpcomingEvent, navController: NavController) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable { navController.navigate(Screen.EventDetails.createRoute(event.title)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = event.imageId),
                contentDescription = event.title,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = event.title, style = MaterialTheme.typography.titleMedium)
                Text(text = event.date, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}