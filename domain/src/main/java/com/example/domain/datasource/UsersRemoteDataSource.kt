package com.example.domain.datasource

import com.example.domain.api.ApiService
import com.example.domain.utils.ResponseOutput
import com.example.domain.model.User
import retrofit2.Retrofit

open class UsersRemoteDataSource(
    private val apiService: ApiService,
    retrofit: Retrofit,
) : BaseRemoteDataSource(retrofit) {

    suspend fun fetchUsers(): ResponseOutput<List<User>> {
        return getResponse(
            request = { apiService.getUsers() },
            defaultErrorMessage = "Error fetching Characters"
        )
    }
}