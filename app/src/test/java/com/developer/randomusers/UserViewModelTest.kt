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
class UserViewModelTest {

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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchUsersWhenNetworkReturnsDataAndDatabaseIsEmpty() = runTest {
        val fakeNonEmptyRESTClient = FakeNonEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        val actual = observeFlow(viewModel.users, testDispatcher) {
            viewModel.fetchUsers()
        }

        assertEquals(1, actual[0].size)
        assertEquals(fakeUser, actual[0].first())
    }

    @Test
    fun deleteUserSuccessfully() = runTest {
        val fakeNonEmptyRESTClient = FakeNonEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao(storedUsers = listOf(fakeUserEntity)))
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        val actual = observeFlow(viewModel.users, testDispatcher) {
            viewModel.deleteUser(fakeUserEntity.toUser())
        }
        val expected = listOf(
            listOf(fakeUser),
            emptyList()
        )

        assertEquals(actual, expected)
    }

    @Test
    fun updateSearchTextSuccessfully() = runTest {
        val fakeNonEmptyRESTClient = FakeNonEmptyRESTClient()
        val fakeAppDatabase = FakeAppDatabase(FakeUserDao())
        val userRepository = UserRepository(fakeNonEmptyRESTClient, fakeAppDatabase)
        val viewModel = UserViewModel(userRepository, testDispatcher)

        val expected = "test"
        val actual = observeFlow(viewModel.userInputTextForSearch, testDispatcher) {
            viewModel.updateSearchText(expected)

        }
        
        assertEquals(expected, actual.first())
    }
}