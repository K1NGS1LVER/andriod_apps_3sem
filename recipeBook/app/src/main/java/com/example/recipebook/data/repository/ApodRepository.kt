package com.example.recipebook.data.repository

import com.example.recipebook.data.model.ApodDto
import kotlinx.coroutines.flow.Flow

interface ApodRepository {
    fun getLastTenDaysApod(startDate: String, endDate: String): Flow<Result<List<ApodDto>>>
}
