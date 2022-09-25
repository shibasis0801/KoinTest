package com.example.domain.repository

import com.example.domain.datasource.UsersRemoteDataSource
import com.example.domain.model.User
import com.example.domain.utils.ResponseOutput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class UsersRepositoryImpl(
    private val usersRemoteDataSource: UsersRemoteDataSource,
) : UsersRepository {
    override suspend fun fetchUsers(): Flow<ResponseOutput<List<User>>> {
        return flow {
            emit(ResponseOutput.loading())
            val result = usersRemoteDataSource.fetchUsers()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}
