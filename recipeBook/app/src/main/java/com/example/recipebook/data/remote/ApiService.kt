package com.example.recipebook.data.remote

import com.example.recipebook.data.model.ApodDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Fetch a range of APOD entries.
     * @param startDate ISO-8601 date string "YYYY-MM-DD"
     * @param endDate   ISO-8601 date string "YYYY-MM-DD"
     * @param apiKey    NASA API key (defaults to DEMO_KEY)
     */
    @GET("planetary/apod")
    suspend fun getApodRange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): List<ApodDto>
}
