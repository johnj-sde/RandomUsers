package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.model.Picture
import com.developer.randomusers.model.User
import com.developer.randomusers.network.model.toUserEntity
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.network.model.toId
import com.developer.randomusers.network.model.toName
import com.developer.randomusers.network.model.toPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class UserRepository(
    val restClient: RandomUserAPIClient,
    val database: AppDatabase
) {
    val users = database.userDao().getAll().map {
        list ->
        list.map { userEntity ->
            User(
                id = userEntity.id.toId(),
                phone = userEntity.phone,
                email = userEntity.email,
                picture = userEntity.pictureHttpResponse?.toPicture(),
                name = userEntity.nameHttpResponse?.toName(),
                gender = userEntity.gender
            )
        }
    }

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
            result.getOrNull()?.userHttpResponses ?: emptyList()
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