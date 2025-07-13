package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.database.model.toId
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.network.model.toName
import com.developer.randomusers.network.model.toPicture
import com.developer.randomusers.network.model.toUserEntity
import kotlinx.coroutines.flow.map


class UserRepository(
    val restClient: RandomUserAPIClient,
    val database: AppDatabase
) {
    val users = database.userDao().getAll().map {
        list ->
        list
            .filter { userEntity ->
                !userEntity.wasDeleted
            }
            .map { userEntity ->
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

    suspend fun loadUsers() {
        fetchUsers()
    }

    private suspend fun fetchUsers() {
        val result = runCatching { restClient.fetchUsers(40) }

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

        dao.insertAll(latestUserEntities)
    }

    fun deleteUser(user: User) {
        val dao = database.userDao()
        dao.markUserWithIdAsDeleted(user.id.name, user.id.value)
    }
}