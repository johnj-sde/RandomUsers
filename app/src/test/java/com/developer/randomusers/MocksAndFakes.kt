package com.developer.randomusers

import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.database.dao.UserDao
import com.developer.randomusers.database.model.UserEntity
import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.network.model.IdHttpResponse
import com.developer.randomusers.network.model.NameHttpResponse
import com.developer.randomusers.network.model.ResultsAndInfoHttpResponse
import com.developer.randomusers.network.model.UserHttpResponse
import com.developer.randomusers.network.model.toUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import kotlin.math.exp


val fakeNameHttpResponse = NameHttpResponse(
    title = "Mrs.",
    first = "Fake",
    last = "Name"
)

val fakeIdHttpResponse = IdHttpResponse(
    name = "fakeNameAttributeForIdHttpResponse1",
    value = "fakeNameValueForIdHttpResponse1"
)

val fakeIdHttpResponseWithEmptyName = IdHttpResponse(
    name = "",
    value = "fakeIdHttpResponseWithEmptyName"
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

val fakeUserHttpResponseWithIdWithEmptyName = UserHttpResponse(
    gender = "male",
    nameHttpResponse = fakeNameHttpResponse,
    locationHttpResponse = null,
    email = "fakename2@example.com",
    loginHttpResponse = null,
    dob = null,
    registeredHttpResponse = null,
    phone = "+1 555 555 2342",
    cell = "+1 555 555 3245",
    id = fakeIdHttpResponseWithEmptyName,
    pictureHttpResponse = null,
    nat = null
)

val fakeUserEntity = fakeUserHttpResponse.toUserEntity()

val fakeUser = fakeUserHttpResponse.toUserEntity().toUser()

val fakeNonEmptyNetworkResults = ResultsAndInfoHttpResponse(
    userHttpResponses = arrayListOf(fakeUserHttpResponse)
)

class InMemoryRESTClient(
    private val expectedResults: List<User> = emptyList()
) : RandomUserAPIClientInterface {

    private var isUnavailable = false
    private var isOffline = false

    override suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse {
        if (isUnavailable) throw HttpException(Response.error<String>(401, "".toResponseBody()))
        if (isOffline) throw IOException()
        return ResultsAndInfoHttpResponse(
            userHttpResponses = expectedResults.map {
                UserHttpResponse(
                    id = IdHttpResponse(
                        name = it.id.name,
                        value = it.id.value
                    )
                )
            }
        )
    }

    fun setUnavailable(){
        isUnavailable = true
    }

    fun setOffline() {
        isOffline = true
    }

}

class FakeNonErrorRESTClient(
    val networkResults: ResultsAndInfoHttpResponse
): RandomUserAPIClientInterface {
    override suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse {
        return networkResults
    }
}

val fakeEmptyRESTClient = FakeNonErrorRESTClient(
    networkResults = ResultsAndInfoHttpResponse(userHttpResponses = arrayListOf())
)

val fakeNonEmptyResponseRESTClient = FakeNonErrorRESTClient(
    networkResults = fakeNonEmptyNetworkResults
)

class ErrorRESTClient: RandomUserAPIClientInterface{
    override suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse {
        throw Exception("testing error case")
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
