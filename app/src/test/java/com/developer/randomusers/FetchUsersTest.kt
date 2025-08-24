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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
class FetchUsersTest {

    private val emptyListOfUser = emptyList<User>()
    private val nonEmptyListOfUser = listOf(fakeUserHttpResponse.toUserEntity().toUser())


    @Test
    fun initialUsersStateIsDefault() {
        val fakeRESTClient = FakeEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase()
        val userRepository = UserRepository(fakeRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository)

        assertEquals(emptyListOfUser, viewModel.users.value)
    }

    @Test
    fun fetchUsersWhenNetworkReturnsDataAndDatabaseIsEmpty() = runTest {
        val fakeNonEmptyRESTClient = FakeNonEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase()
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository)
        println("in ${FetchUsersTest::class.simpleName}: pre test")

        val actual = mutableListOf<List<User>>()

        viewModel.fetchUsers()



        viewModel.users.collectLatest { list ->
            println("in ${FetchUsersTest::class.simpleName}: list coming in $list")
            actual.add(list)
        }

        assertEquals(nonEmptyListOfUser, actual[0].toList())

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

class FakeAppDatabase: AppDatabaseInterface {
    override fun userDao(): UserDao {
        return FakeUserDao()
    }

}

class FakeUserDao: UserDao {

    private var fakeUserListInDatabase: MutableList<UserEntity> = mutableListOf()

    override fun getAll(): Flow<List<UserEntity>> {
        println("in ${FakeUserDao::class.simpleName}: getAll users in FakeDao $fakeUserListInDatabase")
        return flow {fakeUserListInDatabase.toList()}
    }

    override fun insertAll(users: List<UserEntity>) {
        println("in ${FakeUserDao::class.simpleName}: inserting users in FakeDao $users")
        fakeUserListInDatabase.addAll(users)
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
