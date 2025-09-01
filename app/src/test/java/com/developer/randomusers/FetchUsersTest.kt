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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
class FetchUsersTest {

    private val emptyListOfUser = emptyList<User>()
    private val nonEmptyListOfUser = listOf(fakeUserHttpResponse.toUserEntity().toUser())

    val testDispatcher = Dispatchers.Unconfined //StandardTestDispatcher()
    val testScope = CoroutineScope(testDispatcher)


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
        assertEquals(fakeUserHttpResponse.toUserEntity().toUser(), viewModel.users.value[0])


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

class FakeUserDao: UserDao {

    private var fakeUserListInDatabase: MutableList<UserEntity> = mutableListOf()
    private val _allUsersFlow: MutableStateFlow<List<UserEntity>> = MutableStateFlow(fakeUserListInDatabase)

    override fun getAll(): Flow<List<UserEntity>> {
        println("\nin ${FakeUserDao::class.simpleName}: getAll users in FakeDao $fakeUserListInDatabase \n")
      //  return flow {fakeUserListInDatabase.toList()}
        return _allUsersFlow
    }

    override fun insertAll(users: List<UserEntity>) {
        println("\nin ${FakeUserDao::class.simpleName}: inserting users in FakeDao $users \n")
        // fakeUserListInDatabase.addAll(users)
        val latestList = mutableListOf<UserEntity>()
        latestList.addAll(fakeUserListInDatabase)
        latestList.addAll(users)
        fakeUserListInDatabase = latestList
        _allUsersFlow.value = latestList
    }

    override fun markUserWithIdAsDeleted(name: String, value: String) {
        TODO("Not yet implemented")
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

val fakeNonEmptyNetworkResults = ResultsAndInfoHttpResponse(
    userHttpResponses = arrayListOf(fakeUserHttpResponse)
)
