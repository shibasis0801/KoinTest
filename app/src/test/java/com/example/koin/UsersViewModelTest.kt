package com.example.koin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.di.datasourceModule
import com.example.domain.di.networkModule
import com.example.domain.di.repoModule
import com.example.domain.di.useCaseModule
import com.example.domain.usecase.UsersUseCase
import com.example.domain.utils.NetworkHelper
import com.example.domain.utils.ResponseOutput
import com.example.koin.DummyData.getDummyUsers
import com.example.koin.di.viewModelModule
import com.example.koin.users.UsersViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UsersViewModelTest : KoinTest {

    private val networkHelper: NetworkHelper by inject()
    private val usersUseCase: UsersUseCase by inject()
    private val usersViewModel: UsersViewModel by inject()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        startKoin {
            listOf(
                networkModule,
                datasourceModule,
                repoModule,
                useCaseModule,
                viewModelModule
            )
        }
    }

    @Test
    fun `Given users when fetchUsers should return Success`() = runTest {
        val flowQuestions = flowOf(ResponseOutput.success(getDummyUsers()))
        Mockito.doReturn(flowQuestions).`when`(usersUseCase).execute()
        usersViewModel.fetchUsers()
        assert(1 == usersViewModel.usersList.value?.data?.size)
    }
}