package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.model.toUserEntity
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class UserRepository(
    val restClient: RandomUserAPIClient,
    val database: AppDatabase
) {
    private val _users = database.userDao().getAll()
    val users = _users

    private val mutex = Mutex()

    suspend fun loadUsers() = mutex.withLock {
        withContext(Dispatchers.IO){
            fetchUsers()
        }

    }

    private suspend fun fetchUsers() {
        val result = withContext(Dispatchers.IO) {
            runCatching { restClient.fetchUsers(40) }
        }

        val latestUsersList = if (result.isSuccess) {
            result.getOrNull()?.users ?: emptyList()
        } else {
            emptyList()
        }

        val latestUserEntities = latestUsersList
            .filter { user ->
                !user.id.name.isEmpty() && user.id.value!=null
            }
            .map { user ->
                user.toUserEntity()
            }

        val dao = database.userDao()

        withContext(Dispatchers.IO) {
            latestUserEntities.forEach {
                println(it)
            }
            dao.insertAll(latestUserEntities)
        }
    }
}