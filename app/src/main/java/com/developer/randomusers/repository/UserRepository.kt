package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.database.model.UserEntity
import com.developer.randomusers.model.User
import com.developer.randomusers.model.toUserEntity
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class UserRepository(
    val restClient: RandomUserAPIClient,
    val database: AppDatabase
) {
    private val _users = database.userDao().getAll()
    //TODO(): find way to convert to state flow; cold flow to state flow
    val users = _users

    private val _fetchedUsers = MutableStateFlow<List<User>>(emptyList())
    val fetchedUsers = _fetchedUsers.asStateFlow()

    private var haveUsersLoadedFromLocalDatabase = false

    private val mutex = Mutex()

    suspend fun loadUsers() = mutex.withLock {
        withContext(Dispatchers.IO){
            println("loading users")

           /* if (!haveUsersLoadedFromLocalDatabase) {
                val dao = database.userDao()
                val userFlow = dao.getAll()

                haveUsersLoadedFromLocalDatabase = true
            }*/
            fetchUsers()
        }

    }

    private suspend fun fetchUsers() {
        println("fetching users...")
        val result = withContext(Dispatchers.IO) {
            runCatching { restClient.fetchUsers(40) }
        }

        val latestUsersList = if (result.isSuccess) {
            result.getOrNull()?.users ?: emptyList()
        } else {
            emptyList()
        }

        val latestUserEntities = latestUsersList.map { user ->
            user.toUserEntity()
        }

        val dao = database.userDao()

        withContext(Dispatchers.IO) {
            println("inserting to room")
            latestUserEntities.forEach {
                println(it)
            }
            dao.insertAll(latestUserEntities)
        }
    }
}