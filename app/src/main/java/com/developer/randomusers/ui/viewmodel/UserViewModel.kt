package com.developer.randomusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(
    val userRepository: UserRepository
): ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            val result = userRepository.fetchUsers()
            val latestUsersList = if (result.isSuccess) {
                result.getOrNull()?.users ?: emptyList()
            } else {
                emptyList()
            }
            _users.update { currUsersList ->
                val mutableListOfCurrUsers = currUsersList.toMutableList()
                mutableListOfCurrUsers.addAll(latestUsersList)
                mutableListOfCurrUsers.toList()

            }
        }

    }

}