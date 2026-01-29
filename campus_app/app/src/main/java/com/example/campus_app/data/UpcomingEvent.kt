package com.example.campus_app.data

import androidx.annotation.DrawableRes

data class UpcomingEvent(
    val title: String,
    val date: String,
    @DrawableRes val imageId: Int
)
