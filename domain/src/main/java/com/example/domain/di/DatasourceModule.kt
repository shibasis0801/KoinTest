package com.example.domain.di

import com.example.domain.datasource.UsersRemoteDataSource
import org.koin.dsl.module

val datasourceModule = module {
    single {
        UsersRemoteDataSource(
            apiService = get(),
            retrofit = get()
        )
    }
}