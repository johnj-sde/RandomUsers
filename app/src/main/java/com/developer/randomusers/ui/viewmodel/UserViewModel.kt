package com.developer.randomusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.User
import com.developer.randomusers.model.UsersState
import com.developer.randomusers.repository.MqttEventRepositoryInterface
import com.developer.randomusers.repository.UserRepositoryInterface
import com.developer.randomusers.ui.screens.NavResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserViewModel(
    val userRepository: UserRepositoryInterface,
    val mqttEventRepository: MqttEventRepositoryInterface,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    val userInputTextForSearch: StateFlow<String>
        get() = _userInputTextForSearch.asStateFlow()
    private val _userInputTextForSearch = MutableStateFlow("")

    private val _errorState = MutableStateFlow<Throwable?>(null)

    private val _result = MutableStateFlow<NavResult>(NavResult.Idle)
    val result: StateFlow<NavResult> = _result

    val usersState = combine(_errorState, userRepository.getUsers()) { error, users ->
            when (error) {
                is IOException -> UsersState(isOfflineError = true)
                is HttpException -> UsersState(isBackendError = true)
                else -> UsersState(users)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            UsersState()
        )

    init {
        viewModelScope.launch {
            withContext(dispatcher) {
                mqttEventRepository.connectMqttClient()
            }
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            withContext(dispatcher) {
                try {
                    userRepository.fetchNewUsers()
                } catch(e: Exception) {
                    _errorState.update { e }
                }
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            withContext(dispatcher) {
                userRepository.deleteUser(user)
            }
        }
    }

    private var job: Job? = null

    fun updateSearchText(userInput: String){
        _userInputTextForSearch.update { userInput }
        job?.cancel()
        job = viewModelScope.launch {
            delay(700L)
            userRepository.filterUsersByUserSearchText(userInput)
        }
    }

    // Function called by the popping screen
    fun setNavigationResultToBackPressed() {
        _result.update { NavResult.BackPressed }
    }

    // Function called by the receiving screen to consume the data
    fun consumeNavigationResult() {
        _result.update { NavResult.Idle }
    }

    fun logMqttEvent() {
        mqttEventRepository.publishCommand("This is a published payload from RandomUsers app")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            withContext(dispatcher) {
                mqttEventRepository.disconnectMqttClient()
            }
        }
    }

}