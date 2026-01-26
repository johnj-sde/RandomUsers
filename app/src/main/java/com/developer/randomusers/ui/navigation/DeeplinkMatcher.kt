package com.developer.randomusers.ui.navigation

import androidx.navigation3.runtime.NavKey

interface DeeplinkMatcher {
    fun match(deeplink: String): NavKey?

    fun getDeepLinkRegexString(scheme: String, host: String, pathPattern: String) : String {
        return "$scheme://$host/$pathPattern"
    }
}