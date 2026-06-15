package com.developer.randomusers

import com.developer.randomusers.database.model.toUser
import com.developer.randomusers.model.UsersState
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutineTestExtension::class)
class UserViewModelTest {

    private val testDispatcher = Dispatchers.Unconfined

    @Test
    fun initialUsersStateIsDefault() {
        val fakeRESTClient = InMemoryRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())

        val userRepository = UserRepository(fakeRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        assertEquals(UsersState(), viewModel.usersState.value)
    }

    @Test
    fun fetchUsersWhenNetworkReturnsDataAndDatabaseIsEmpty() = runTest {
        val fakeNonEmptyRESTClient = fakeNonEmptyResponseRESTClient
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())

        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val actual = observeFlow(viewModel.usersState, testDispatcher) {
            viewModel.fetchUsers()
        }

        assertEquals(1, actual[0].users.size)
        assertEquals(fakeUser, actual[0].users.first())
    }

    @Test
    fun fetchUsersWhenNetworksReturnsError() = runTest {
        val errorRESTClient = InMemoryRESTClient().apply { setUnavailable() }
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(restClient = errorRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val actual = observeFlow(viewModel.usersState, testDispatcher) {
            viewModel.fetchUsers()
        }

        assertEquals(true, actual.first().isBackendError)
    }

    @Test
    fun fetchUsersWhenNetworkReturnsUserWithIdWithEmptyName() = runTest {
        val restClient = InMemoryRESTClient(
            expectedResults =  arrayListOf(fakeUserHttpResponseWithIdWithEmptyName)
        )
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(restClient = restClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val actual = observeFlow(viewModel.usersState, testDispatcher) {
            viewModel.fetchUsers()
        }

        assertEquals(0, actual.size)

    }

    @Test
    fun deleteUserSuccessfully() = runTest {
        val fakeNonEmptyRESTClient = fakeNonEmptyResponseRESTClient
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao(storedUsers = listOf(fakeUserEntity)))
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val actualStates = observeFlow(viewModel.usersState, testDispatcher) {
            viewModel.deleteUser(fakeUserEntity.toUser())
        }
        val expected = listOf(
            listOf(fakeUser),
            emptyList()
        )

        val actualUsers = actualStates.map { it.users }

        assertEquals(actualUsers, expected)
    }

    @Test
    fun updateSearchTextSuccessfully() = runTest {
        val fakeNonEmptyRESTClient = InMemoryRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val expected = "test"
        val actual = observeFlow(viewModel.userInputTextForSearch, testDispatcher) {
            viewModel.updateSearchText(expected)
        }

        assertEquals(expected, actual.first())
    }

    @Test
    fun toggleUserAsFavoriteSuccessfully() = runTest {
        val fakeNonEmptyRESTClient = fakeNonEmptyResponseRESTClient
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao(storedUsers = listOf(fakeUserEntity)))
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val actualStates = observeFlow(viewModel.usersState, testDispatcher) {
            viewModel.toggleFavorite(fakeUserEntity.toUser().id)
        }
        val expected = listOf(
            listOf(fakeUser),
            listOf(fakeUser.copy(isFavorite = !fakeUser.isFavorite))
        )

        val actualUsers = actualStates.map { it.users }

        assertEquals(actualUsers, expected)
    }

    @Test
    fun restoreUserSuccessfully() = runTest {
        val fakeNonEmptyRESTClient = fakeNonEmptyResponseRESTClient
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao(storedUsers = listOf(fakeUserEntity)))
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, dummyMqttEventRepository, testDispatcher)

        val actualStates = observeFlow(viewModel.usersState, testDispatcher) {
            viewModel.deleteUser(fakeUserEntity.toUser())
            viewModel.restoreUser(fakeUserEntity.toUser())

        }

        val expected = listOf(
            listOf(fakeUser),
            emptyList(),
            listOf(fakeUser)
        )
//test comment
        val actualUsers = actualStates.map { it.users }

        assertEquals(actualUsers, expected)
    }

}