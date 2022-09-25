package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.utils.ResponseOutput
import kotlinx.coroutines.flow.Flow

interface UsersUseCase {
    suspend fun execute(): Flow<ResponseOutput<List<User>>>
}
