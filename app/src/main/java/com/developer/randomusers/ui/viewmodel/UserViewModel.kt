package com.developer.randomusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.User
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.repository.UserRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    val userRepository: UserRepositoryInterface
): ViewModel() {

    val userInputTextForSearch: StateFlow<String>
        get() = _userInputTextForSearch.asStateFlow()
    private val _userInputTextForSearch = MutableStateFlow<String>("")

    @OptIn(FlowPreview::class)
    val debouncedUserInputTextForSearch: StateFlow<String> = userInputTextForSearch
        .debounce(300L)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = ""
        )

    val users = userRepository.getUsers().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun fetchUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.loadUsers()
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.deleteUser(user)
            }
        }
    }

    fun updateSearchText(userInput: String){
        _userInputTextForSearch.update { userInput }

    }

}