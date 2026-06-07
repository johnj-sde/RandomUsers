package com.developer.randomusers

import com.developer.randomusers.database.dao.UserDao
import com.developer.randomusers.database.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserDao(
    storedUsers: List<UserEntity> = emptyList()
): UserDao {

    private val _allUsersFlow: MutableStateFlow<List<UserEntity>> = MutableStateFlow(storedUsers)

    private val markedForDeletion: MutableSet<UserEntity> = mutableSetOf()

    override fun getAll(): Flow<List<UserEntity>> {
        println("\nin ${FakeUserDao::class.simpleName}: getAll users in FakeDao ${_allUsersFlow.value} \n")
        return _allUsersFlow
    }

    override fun insertAll(users: List<UserEntity>) {
        println("\nin ${FakeUserDao::class.simpleName}: inserting users in FakeDao $users \n")

        val current = _allUsersFlow.value
        val newList = current + users
        _allUsersFlow.update { newList }

        println("_allUsersFlow value : ${_allUsersFlow.value}")
        println("")
    }


    override fun toggleUserWithIdAsFavorite(name: String, value: String) {
        val current = _allUsersFlow.value
        val target = current.firstOrNull { it.id.name == name && it.id.value == value }
        var newList = current
        target?.let {
            newList = newList.mapIndexed { index, item ->
                if (item.id.name == name && item.id.value == value) item.copy(isFavorite = !item.isFavorite) else item
            }
        }
        _allUsersFlow.update { newList }
    }

    override fun markUserWithIdAsDeleted(name: String, value: String, toDelete: Boolean) {
        if (toDelete) {
            val current = _allUsersFlow.value
            val markedUser =
                current.firstOrNull { userEntity -> userEntity.id.name == name && userEntity.id.value == value }
            markedUser?.let {
                markedForDeletion.add(it)
            }
            val newList = current.filter {
                    userEntity ->
                !(userEntity.id.name==name && userEntity.id.value==value)
            }
            _allUsersFlow.update { newList }
        } else {
            val markedUser = markedForDeletion.firstOrNull { userEntity -> userEntity.id.name == name && userEntity.id.value == value }
            markedUser?.let {
                val current = _allUsersFlow.value
                val newList = current + it
                _allUsersFlow.update { newList }
            }
        }


    }
}
