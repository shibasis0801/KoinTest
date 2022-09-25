package com.example.domain.di

import com.example.domain.usecase.UsersUseCase
import com.example.domain.usecase.UsersUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<UsersUseCase> {
        return@single UsersUseCaseImpl(get())
    }
}