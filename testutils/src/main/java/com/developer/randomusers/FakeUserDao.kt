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

    override fun markUserWithIdAsDeleted(name: String, value: String) {
        val current = _allUsersFlow.value
        val newList = current.filter {
                userEntity ->
            !(userEntity.id.name==name && userEntity.id.value==value)
        }
        _allUsersFlow.update { newList }
    }
}
