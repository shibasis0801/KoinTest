package com.example.domain.di

import com.example.domain.repository.UsersRepository
import com.example.domain.repository.UsersRepositoryImpl
import org.koin.dsl.module

val repoModule = module {
    single<UsersRepository> {
        return@single UsersRepositoryImpl(get())
    }
}