package com.example.domain.api

import com.example.domain.model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("p68409/movies")
    suspend fun getUsers(): Response<List<User>>
}
