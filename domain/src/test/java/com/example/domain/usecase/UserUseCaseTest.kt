package com.example.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.model.DummyData.getDummyUsers
import com.example.domain.repository.UsersRepository
import com.example.domain.utils.ResponseOutput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserUseCaseTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var usersRepository: UsersRepository
    private lateinit var usersUseCase: UsersUseCaseImpl

    @Before
    fun setUp() {
        usersUseCase = UsersUseCaseImpl(usersRepository)
    }

    @Test
    fun `Given users When userUseCase returns Success`() = runBlocking {
        val inputFlow = flowOf(ResponseOutput.success(getDummyUsers()))
        Mockito.`when`(usersRepository.fetchUsers()).thenReturn(inputFlow)
        val output = usersUseCase.execute().toList()
        assert(output[0].data?.size == 1)
    }
}