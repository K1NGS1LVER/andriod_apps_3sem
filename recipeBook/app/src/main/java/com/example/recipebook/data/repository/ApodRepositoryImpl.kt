package com.example.recipebook.data.repository

import com.example.recipebook.data.model.ApodDto
import com.example.recipebook.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApodRepositoryImpl(private val apiService: ApiService) : ApodRepository {

    override fun getLastTenDaysApod(startDate: String, endDate: String): Flow<Result<List<ApodDto>>> =
        flow {
            try {
                val response = apiService.getApodRange(
                    startDate = startDate,
                    endDate = endDate
                )
                // Return entries in reverse-chronological order (newest first)
                emit(Result.success(response.reversed()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
}
