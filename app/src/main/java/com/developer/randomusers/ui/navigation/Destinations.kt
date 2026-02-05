package com.developer.randomusers.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(val idComponent1: String, val idComponent2: String): NavKey

@Serializable
data class UserList(val searchFilter: String): NavKey