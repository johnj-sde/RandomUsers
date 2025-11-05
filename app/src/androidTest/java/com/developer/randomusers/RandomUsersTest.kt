package com.developer.randomusers

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.model.Id
import com.developer.randomusers.model.Name
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.repository.UserRepositoryInterface
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
                typeSearchQuery(name, true)
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
            typeSearchQuery(unusedName, false)
        } verify {
            userFullNameAndTitleIsNotDisplayed(unusedUser)
            userEmailIsNotDisplayed(unusedUser)
            userFullNameAndTitleIsNotDisplayed(fakeUser)
            userFullNameAndTitleIsNotDisplayed(fakeUser)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun should_open_user_details_screen() {
        launchUserLists(rule) {
            tapOnUser(fakeUser)
        } verify {
            userGenderIsDisplayed(fakeUser)
            userFullNameAndTitleIsDisplayed(fakeUser)
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun should_swipe_left_to_show_delete_icon() {
        launchUserLists(rule) {
            swipeLeftOnUser(fakeUser)
        } verify {
            deleteIconIsDisplayed()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun should_delete_user_on_tap_delete_icon(){
        launchUserLists(rule) {
            swipeLeftOnUser(fakeUser)
            tapDeleteIcon()
        } verify {
            userFullNameAndTitleIsNotDisplayed(fakeUser)
        }
    }


}

