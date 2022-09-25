package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.utils.ResponseOutput
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun fetchUsers(): Flow<ResponseOutput<List<User>>>
}