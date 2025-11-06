package com.developer.randomusers

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName


private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>


fun launchUserLists(
    rule: MainActivityRule,
    block: RandomUsersRobot.() -> Unit
): RandomUsersRobot {
    return RandomUsersRobot(rule).apply(block)
}

@OptIn(ExperimentalTestApi::class)
class RandomUsersRobot(
    private val rule: MainActivityRule
) {

    infix fun verify(
        function: RandomUsersVerificationRobot.() -> Unit
    ): RandomUsersVerificationRobot {
        return RandomUsersVerificationRobot(rule).apply(function)
    }

    @OptIn(ExperimentalTestApi::class)
    fun typeSearchQuery(query: String) {
        rule.onNodeWithTag("searchText")
            .requestFocus()
            .performTextInput(query)
    }

    fun tapOnUser(user: User) {
        rule.onNodeWithText(user.getFullName()).performClick()
    }

    fun swipeLeftOnUser(user: User) {
        rule.onNodeWithText(user.getFullName()).performTouchInput {
            swipeLeft()
        }
        rule.waitUntilExactlyOneExists(hasTestTag("deleteIcon"))
    }

    fun tapDeleteIcon(){
        rule.onNodeWithTag("deleteIcon").performClick()
    }

}

@OptIn(ExperimentalTestApi::class)
class RandomUsersVerificationRobot(
    private val rule: MainActivityRule
) {

    fun userEmailIsDisplayed(user: User) {
        user.email?.let { email ->
            rule.waitUntilAtLeastOneExists(hasText(email, ignoreCase = true, substring = true))
            rule.onAllNodesWithText(email).assertAll(hasText(email))
        }
    }

    fun userFullNameAndTitleIsDisplayed(user: User) {
        rule.waitUntilAtLeastOneExists(hasText(user.getFullName(), ignoreCase = true, substring = true))
        rule.onAllNodesWithText(user.getFullName()).assertAll(hasText(user.getFullName()))
    }

    fun userEmailIsNotDisplayed(user: User) {
        user.email?.let { email ->
            rule.waitUntilDoesNotExist(hasText(email, ignoreCase = true, substring = true))
            rule.onNodeWithText(email).assertDoesNotExist()
        }
    }

    fun userFullNameAndTitleIsNotDisplayed(user: User) {
        rule.waitUntilDoesNotExist(hasText(user.getFullName(), ignoreCase = true, substring = true))
        rule.onNodeWithText(user.getFullName()).assertDoesNotExist()
    }

    fun userGenderIsDisplayed(user: User) {
        user.gender?.let { gender ->
            rule.waitUntilAtLeastOneExists(hasText(gender, ignoreCase = true, substring = true))
            rule.onNodeWithText(gender).assertExists()
        }
    }

    fun deleteIconIsDisplayed() {
        rule.onNodeWithTag("deleteIcon").isDisplayed()
    }

}