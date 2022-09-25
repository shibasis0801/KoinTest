package com.example.koin.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.usecase.UsersUseCase
import com.example.domain.utils.NetworkHelper
import com.example.domain.utils.ResponseOutput
import com.example.koin.base.BaseViewModel
import kotlinx.coroutines.launch

class UsersViewModel(
    private val networkHelper: NetworkHelper,
    private val usersUseCase: UsersUseCase,
) : BaseViewModel() {

    private val _usersList = MutableLiveData<ResponseOutput<List<User>>>()
    val usersList: LiveData<ResponseOutput<List<User>>> = _usersList

    init {
        Log.d("zzz", "Main View Model init()")
        fetchUsers()
    }

    fun fetchUsers() {
        Log.d("zzz", "Main View Model fetchUsers()")
        viewModelScope.launch {
            Log.d("zzz", "Main View Model fetchUsers posting loading")
            Log.d("zzz",
                "Main View Model fetchUsers isNetworkConnected ${networkHelper.isNetworkConnected()}")
            if (networkHelper.isNetworkConnected()) {
                Log.d("zzz", "Main View Model fetchUsers posting internet present")
                usersUseCase.execute().collect {
                    _usersList.value = it
                }
            } else {
                Log.d("zzz", "Main View Model fetchUsers posting no internet")
                _usersList.postValue(ResponseOutput.error("No internet connection", null))
            }
        }
    }
}
