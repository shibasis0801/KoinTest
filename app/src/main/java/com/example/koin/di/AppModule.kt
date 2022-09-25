package com.example.koin.di

import com.example.domain.di.datasourceModule
import com.example.domain.di.networkModule
import com.example.domain.di.repoModule
import com.example.domain.di.useCaseModule

val appModule = networkModule + datasourceModule + repoModule + useCaseModule + viewModelModule