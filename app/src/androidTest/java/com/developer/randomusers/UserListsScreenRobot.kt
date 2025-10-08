package com.developer.randomusers

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName


private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@DslMarker
annotation class UserListsScreenRobot

fun launchUserLists(
    rule: MainActivityRule,
    block: UserListsRobot.() -> Unit
): UserListsRobot {
    return UserListsRobot(rule).apply(block)
}

@UserListsScreenRobot
class UserListsRobot(
    private val rule: MainActivityRule
) {

    infix fun verify(
        function: UserListsVerificationRobot.() -> Unit
    ): UserListsVerificationRobot {
        return UserListsVerificationRobot(rule).apply(function)
    }
}

@UserListsScreenRobot
class UserListsVerificationRobot(
    private val rule: MainActivityRule
) {

    fun userEmailIsDisplayed(user: User) {
        user.email?.let { email ->
            rule.onAllNodesWithText(email).assertAll(hasText(email))
        }
    }

    fun userFullNameAndTitleIsDisplayed(user: User) {
        rule.onAllNodesWithText(user.getFullName()).assertAll(hasText(user.getFullName()))
    }
}