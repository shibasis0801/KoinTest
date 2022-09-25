package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.UsersRepository
import com.example.domain.usecase.UsersUseCase
import com.example.domain.utils.ResponseOutput
import kotlinx.coroutines.flow.Flow

internal class UsersUseCaseImpl(
    private val usersRepository: UsersRepository,
) : UsersUseCase {

    override suspend fun execute(): Flow<ResponseOutput<List<User>>> {
        return usersRepository.fetchUsers()
    }
}
