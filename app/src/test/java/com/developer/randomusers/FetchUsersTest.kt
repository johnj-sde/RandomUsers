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
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
class FetchUsersTest {

    private val emptyListOfUser = emptyList<User>()

    val testDispatcher = Dispatchers.Unconfined //StandardTestDispatcher()

    @Test
    fun initialUsersStateIsDefault() {
        val fakeRESTClient = FakeEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(fakeRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        assertEquals(emptyListOfUser, viewModel.users.value)
    }

    fun <T> CoroutineScope.observeFlow(
        flow: Flow<T>
    ) {
        val job = launch(testDispatcher) {
            flow.collect {  }
        }
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchUsersWhenNetworkReturnsDataAndDatabaseIsEmpty() = runTest {
        val fakeNonEmptyRESTClient = FakeNonEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        //viewModel.fetchUsers()

        observeFlow(viewModel.users)

        viewModel.fetchUsers()

        assertEquals(1, viewModel.users.value.size)
        assertEquals(fakeUser, viewModel.users.value[0])

    }

    @Test
    fun deleteUserWhenDatabaseIsEmpty() {
        val fakeRESTClient = FakeEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(fakeRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        assertEquals(0, viewModel.users.value.size)
        viewModel.deleteUser(fakeUser)
        assertEquals(0, viewModel.users.value.size)
    }

    @Test
    fun deleteUserWhenDatabaseHasData() = runTest {
        val fakeNonEmptyRESTClient = FakeNonEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao(fakeNonEmptyUserEntityList))
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        observeFlow(viewModel.users)

        assertEquals(1, viewModel.users.value.size)
        viewModel.deleteUser(fakeUser)
        assertEquals(0, viewModel.users.value.size)
    }
}

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
            userEntity.id.name!=name || userEntity.id.value!=value
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

val fakeNonEmptyUserEntityList = listOf(fakeUserEntity)

val fakeUser = fakeUserHttpResponse.toUserEntity().toUser()

val fakeNonEmptyNetworkResults = ResultsAndInfoHttpResponse(
    userHttpResponses = arrayListOf(fakeUserHttpResponse)
)
