package com.example.recipebook.data.model

import com.google.gson.annotations.SerializedName

data class ApodDto(
    @SerializedName("title") val title: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("url") val url: String,
    @SerializedName("hdurl") val hdurl: String?,
    @SerializedName("date") val date: String,
    @SerializedName("media_type") val mediaType: String
)
