package com.developer.randomusers.ui.navigation

import androidx.navigation3.runtime.NavKey



object UserDetailsMatcher: DeeplinkMatcher {

    val regexString = getDeepLinkRegexString(DEEPLINK_SCHEME, DEEPLINK_HOST, "/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)")

    override fun match(deeplink: String): NavKey? {
        val regex = regexString.toRegex()
        val matchResult = regex.find(deeplink)
        return matchResult?.let { match ->
            val (idComponent1, idComponent2) = match.destructured
            return UserDetails(idComponent1, idComponent2)
        }
    }
}