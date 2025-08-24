package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.database.model.toId
import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.network.model.toName
import com.developer.randomusers.network.model.toPicture
import com.developer.randomusers.network.model.toUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepository(
    val restClient: RandomUserAPIClientInterface,
    val database: AppDatabaseInterface
): UserRepositoryInterface {
    private val users = database.userDao().getAll().map {
        list ->
        list
            .filter { userEntity ->
                !userEntity.wasDeleted
            }
            .map { userEntity -> userEntity.toUser() }
    }

    override fun getUsers(): Flow<List<User>> {
        return users
    }

    override suspend fun loadUsers() {
        fetchUsers()
    }

    private suspend fun fetchUsers() {
        val result = runCatching { restClient.fetchUsers(40) }
        println("in ${UserRepository::class.simpleName}: in fetchUsers result is...\n $result")

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

        println("in ${UserRepository::class.simpleName}: in fetchUsers latestUserEntities is...\n $latestUserEntities")
        dao.insertAll(latestUserEntities)
    }

    override fun deleteUser(user: User) {
        val dao = database.userDao()
        dao.markUserWithIdAsDeleted(user.id.name, user.id.value)
    }
}