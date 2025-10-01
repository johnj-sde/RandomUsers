package com.developer.randomusers

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.developer.randomusers.database.AppDatabaseInterface
import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.repository.UserRepository
import com.developer.randomusers.repository.UserRepositoryInterface
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module


@RunWith(AndroidJUnit4::class)
class UserListsScreenTest {

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
    fun should_display_loaded_songs() {
        launchUserLists(rule) { } verify {
            userFullNameAndTitleIsDisplayed(fakeUser)
            userEmailIsDisplayed(fakeUser)
        }
    }

}

