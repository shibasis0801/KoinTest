package com.example.koin

import kotlinx.coroutines.delay

class TestFile {

    suspend fun fetchData(): String {
        delay(1000L)
        return "Hello world"
    }
}