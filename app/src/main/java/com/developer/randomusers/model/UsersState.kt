package com.developer.randomusers.model

data class UsersState(
    val users: List<User> = emptyList(),
    val isOfflineError: Boolean = false,
    val isBackendError: Boolean = false
)
