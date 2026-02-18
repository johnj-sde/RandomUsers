package com.developer.randomusers.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(val idName: String = "", val idValue: String = "", val position: Int = -1): NavKey

@Serializable
data class UserList(val searchFilter: String): NavKey