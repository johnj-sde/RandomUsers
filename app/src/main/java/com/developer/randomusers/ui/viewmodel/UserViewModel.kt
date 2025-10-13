package com.developer.randomusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.User
import com.developer.randomusers.model.UsersState
import com.developer.randomusers.repository.UserRepositoryInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    val userInputTextForSearch: StateFlow<String>
        get() = _userInputTextForSearch.asStateFlow()
    private val _userInputTextForSearch = MutableStateFlow<String>("")

//    @OptIn(FlowPreview::class)
//    val debouncedUserInputTextForSearch: StateFlow<String> = userInputTextForSearch
//        .debounce(300L)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
//            initialValue = ""
//        )

    private val _errorState = MutableStateFlow<Throwable?>(null)

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

    fun fetchUsers() {
        viewModelScope.launch {
            withContext(dispatcher) {
                try {
                    userRepository.loadUsers()
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

    fun updateSearchText(userInput: String){
        _userInputTextForSearch.update { userInput }

    }

}