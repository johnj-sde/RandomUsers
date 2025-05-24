package com.developer.randomusers.repository

import com.developer.randomusers.database.AppDatabase
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    val restClient: RandomUserAPIClient,
    val database: AppDatabase
) {

    suspend fun fetchUsers(): Result<ResultsAndInfo> {
        return withContext(Dispatchers.IO) {
            runCatching { restClient.fetchUsers(40) }
        }
    }
}