package com.example.campus_app.data

import androidx.annotation.DrawableRes

data class Event(
    val title: String,
    val date: String,
    val time: String,
    val venue: String,
    @DrawableRes val imageId: Int
)
