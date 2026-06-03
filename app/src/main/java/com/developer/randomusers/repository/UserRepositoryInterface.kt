package com.developer.randomusers.repository

import com.developer.randomusers.model.Id
import com.developer.randomusers.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {

    fun filterUsersByUserSearchText(userSearch: String)

    fun getUsers(): Flow<List<User>>

    suspend fun fetchNewUsers()

    fun deleteUser(user: User)

    fun restoreUser(user: User)

    fun toggleFavorite(id: Id)
}