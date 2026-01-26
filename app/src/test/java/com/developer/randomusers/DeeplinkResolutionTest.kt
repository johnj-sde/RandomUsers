package com.developer.randomusers

import com.developer.randomusers.ui.navigation.DEEPLINK_HOST
import com.developer.randomusers.ui.navigation.DEEPLINK_SCHEME
import com.developer.randomusers.ui.navigation.DeeplinkResolver
import com.developer.randomusers.ui.navigation.UserDetails
import com.developer.randomusers.ui.navigation.UserList
import org.junit.jupiter.api.Test

class DeeplinkResolutionTest {

    @Test
    fun returnsDefaultDestination() {
        val defaultDestination = UserList
        val deepLinkResolver = DeeplinkResolver(defaultDestination)

        val result = deepLinkResolver.resolve(":irrelevant:")
        val expected = UserList
        val isEquals = expected == result

        assert(isEquals)

    }

    @Test
    fun returnsUserDetailsDestination() {
        val component1 = "SC2X"
        val component2 = "293FAL"

        val userDetailsDeeplink = "$DEEPLINK_SCHEME://$DEEPLINK_HOST/$component1/$component2"
        val deeplinkResolver = DeeplinkResolver(fallbackDestination = UserList)

        val result = deeplinkResolver.resolve(userDetailsDeeplink)
        val expected = UserDetails(
            component1,
            idComponent2 = component2
        )
        val isEquals = expected == result

        assert(isEquals)
    }
}