package com.developer.randomusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    val userRepository: UserRepository
): ViewModel() {

    val users = userRepository.users.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun fetchUsers() {
        viewModelScope.launch {
            userRepository.loadUsers()
        }

    }

}