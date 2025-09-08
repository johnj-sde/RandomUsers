package com.developer.randomusers

import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.database.dao.UserDao
import com.developer.randomusers.database.model.UserEntity
import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.network.model.IdHttpResponse
import com.developer.randomusers.network.model.NameHttpResponse
import com.developer.randomusers.network.model.ResultsAndInfoHttpResponse
import com.developer.randomusers.network.model.UserHttpResponse
import com.developer.randomusers.network.model.toUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeEmptyRESTClient: RandomUserAPIClientInterface {
    override suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse {
        return ResultsAndInfoHttpResponse(userHttpResponses = arrayListOf())
    }
}

class FakeNonEmptyRESTClient: RandomUserAPIClientInterface {
    override suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse {
        return fakeNonEmptyNetworkResults
    }
}

class FakeAppDatabase(val userDao: FakeUserDao): AppDatabaseInterface {
    override fun userDao(): UserDao {
        return userDao
    }

}

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

val fakeNameHttpResponse = NameHttpResponse(
    title = "Mrs.",
    first = "Fake",
    last = "Name"
)

val fakeIdHttpResponse = IdHttpResponse(
    name = "fakeNameAttributeForIdHttpResponse1",
    value = "fakeNameValueForIdHttpResponse1"
)

val fakeUserHttpResponse = UserHttpResponse(
    gender = "female",
    nameHttpResponse = fakeNameHttpResponse,
    locationHttpResponse = null,
    email = "fakename@example.com",
    loginHttpResponse = null,
    dob = null,
    registeredHttpResponse = null,
    phone = "+1 555 555 0100",
    cell = "+1 555 555 0100",
    id = fakeIdHttpResponse,
    pictureHttpResponse = null,
    nat = null
)

val fakeUserEntity = fakeUserHttpResponse.toUserEntity()

val fakeUser = fakeUserHttpResponse.toUserEntity().toUser()

val fakeNonEmptyNetworkResults = ResultsAndInfoHttpResponse(
    userHttpResponses = arrayListOf(fakeUserHttpResponse)
)
