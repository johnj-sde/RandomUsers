package com.developer.randomusers.ui.navigation

import androidx.navigation3.runtime.NavKey

object UserListMatcher: DeeplinkMatcher {
    override fun match(deeplink: String): NavKey? {

        val regexString = getDeepLinkRegexString(DEEPLINK_SCHEME, DEEPLINK_HOST, "(\\?)(searchForName)(=)([a-zA-Z ]+)")
        val regex = Regex(regexString)

        val match = regex.find(deeplink)
        return match?.let { result ->
            val (_, _, _, searchQueryParameterValue) = result.destructured
            UserList(searchFilter = searchQueryParameterValue)
        }
    }
}