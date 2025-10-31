package com.developer.randomusers.repository

import com.developer.randomusers.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {

    fun filterUsersByUserSearchTextAfterDebounce(userSearch: String)

    fun getUsers(): Flow<List<User>>

    suspend fun loadUsers()

    fun deleteUser(user: User)
}