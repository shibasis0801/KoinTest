package com.example.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.datasource.UsersRemoteDataSource
import com.example.domain.di.domainModule
import com.example.domain.model.DummyData.getDummyUsers
import com.example.domain.utils.ResponseOutput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest: KoinTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val userRepository by inject<UsersRepository>()
    private val userRemoteDataSource by inject<UsersRemoteDataSource>()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        startKoin { modules(domainModule) }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `Given users When fetchUsers returns Success`() = runBlocking {
        val mockUsers = getDummyUsers()
        val mockUsersOutput = ResponseOutput.success(mockUsers)
        val inputFlow = listOf(ResponseOutput.loading(), mockUsersOutput)


        val outputFlow = userRepository.fetchUsers().toList()

        assert(outputFlow.size == 2)
        assert(inputFlow[0] == outputFlow[0])
        assert(inputFlow[1] == outputFlow[1])
    }
}
