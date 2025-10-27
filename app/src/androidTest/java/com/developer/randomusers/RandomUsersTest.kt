package com.developer.randomusers

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.model.Id
import com.developer.randomusers.model.Name
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.repository.UserRepositoryInterface
import com.developer.randomusers.ui.viewmodel.UserViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module


@RunWith(AndroidJUnit4::class)
class RandomUsersTest {

    private val userListsModule = module {
        single<RandomUserAPIClientInterface> {
            InMemoryRESTClient()
        }

        single<AppDatabaseInterface> {
            FakeAppDatabase(FakeUserDao(storedUsers = listOf(fakeUserEntity)))
        }

        single<UserRepositoryInterface> {
            UserRepository(restClient = get(), database = get())
        }

    }

    @get:Rule(order = 1)
    val koinRule = KoinTestRule(listOf(userListsModule))

    @get:Rule(order = 2)
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun should_display_loaded_users() {
        launchUserLists(rule) { } verify {
            userFullNameAndTitleIsDisplayed(fakeUser)
            userEmailIsDisplayed(fakeUser)
        }
    }

    @Test
    fun should_search_and_match_user() {
        val queryMatchingFirstUser = fakeUser.name?.first
        queryMatchingFirstUser?.let { name ->
            launchUserLists(rule) {
                typeSearchQuery(name)
            } verify {
                userFullNameAndTitleIsDisplayed(fakeUser)
                userEmailIsDisplayed(fakeUser)
            }
        }
    }

    @Test
    fun should_search_and_fail_to_match_user() {
        val unusedUser = User(
            id = Id(
                name = "unusedUser_id1_name",
                value = "unusedUser_id1_value"
            ),
            name = Name(
                first = "Unused",
                last = "Username"
            )
        )
        val unusedName = "Unused Name"
        launchUserLists(rule) {
            typeSearchQuery(unusedName)
        } verify {
            userFullNameAndTitleIsNotDisplayed(unusedUser)
            userEmailIsNotDisplayed(unusedUser)
            userFullNameAndTitleIsNotDisplayed(fakeUser)
            userFullNameAndTitleIsNotDisplayed(fakeUser)
        }

    }

}

