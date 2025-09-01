package com.developer.randomusers.network

import com.developer.randomusers.network.model.ResultsAndInfoHttpResponse
import retrofit2.http.Query

interface RandomUserAPIClientInterface {

    suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse

}