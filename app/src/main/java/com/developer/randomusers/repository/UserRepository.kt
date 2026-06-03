package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.model.Id
import com.developer.randomusers.model.User
import com.developer.randomusers.model.matchesName
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.network.model.UserHttpResponse
import com.developer.randomusers.network.model.toUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class UserRepository(
    val restClient: RandomUserAPIClientInterface,
    val database: AppDatabaseInterface
): UserRepositoryInterface {

    private val _userSearchTextFilter = MutableStateFlow("")

    private val _allUsers = database.userDao().getAll().map {
        list ->
        list
            .filter { userEntity ->
                !userEntity.wasDeleted
            }
            .map { userEntity -> userEntity.toUser() }
    }

    private val _users = combine(_userSearchTextFilter, _allUsers) { filter, allUsers ->
        allUsers.filter { user -> user.matchesName(filter) }
    }

    override fun filterUsersByUserSearchText(userSearch: String) {
        _userSearchTextFilter.update { userSearch }
    }

    override fun getUsers(): Flow<List<User>> {
        return _users
    }

    override suspend fun fetchNewUsers() {
        fetchUsersFromNetwork()
    }

    private suspend fun fetchUsersFromNetwork() {
        val result = runCatching { restClient.fetchUsers(40) }
        val latestUsersList = mutableListOf<UserHttpResponse>()
        if (result.isSuccess) {
            latestUsersList.addAll(result.getOrNull()?.userHttpResponses ?: emptyList())
        } else {
            result.exceptionOrNull()?.let {
                throw it
            }
        }

        val latestUserEntities = latestUsersList
            .filter { user ->
                !user.id.name.isEmpty()
            }
            .map { user ->
                user.toUserEntity()
            }

        val dao = database.userDao()

    //    println("in ${UserRepository::class.simpleName}: in fetchUsers latestUserEntities is...\n $latestUserEntities")
        dao.insertAll(latestUserEntities)
    }

    override fun deleteUser(user: User) {
        val dao = database.userDao()
        dao.markUserWithIdAsDeleted(user.id.name, user.id.value)
    }

    override fun toggleFavorite(id: Id) {
        val dao = database.userDao()
        dao.toggleUserWithIdAsFavorite(id.name, id.value)
    }
}