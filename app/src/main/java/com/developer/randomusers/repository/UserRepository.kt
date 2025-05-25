package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class UserRepository(
    val restClient: RandomUserAPIClient,
    val database: AppDatabase
) {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    suspend fun fetchUsers() {
        val result = withContext(Dispatchers.IO) {
            runCatching { restClient.fetchUsers(40) }
        }

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