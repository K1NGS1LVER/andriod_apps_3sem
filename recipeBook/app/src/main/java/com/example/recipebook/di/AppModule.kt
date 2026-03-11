package com.example.recipebook.di

import com.example.recipebook.data.remote.ApiService
import com.example.recipebook.data.repository.ApodRepository
import com.example.recipebook.data.repository.ApodRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val BASE_URL = "https://api.nasa.gov/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val repository: ApodRepository by lazy {
        ApodRepositoryImpl(apiService)
    }
}
