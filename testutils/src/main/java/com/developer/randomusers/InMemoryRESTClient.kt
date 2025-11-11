package com.developer.randomusers

import com.developer.randomusers.network.RandomUserAPIClientInterface
import com.developer.randomusers.network.model.ResultsAndInfoHttpResponse
import com.developer.randomusers.network.model.UserHttpResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import okio.IOException


class InMemoryRESTClient(
    private val expectedResults: List<UserHttpResponse> = emptyList()
) : RandomUserAPIClientInterface {

    private var isUnavailable = false
    private var isOffline = false

    override suspend fun fetchUsers(limit: Int): ResultsAndInfoHttpResponse {
        if (isUnavailable) throw HttpException(Response.error<String>(401, "".toResponseBody()))
        if (isOffline) throw IOException()
        return ResultsAndInfoHttpResponse(
            userHttpResponses = expectedResults
        )
    }

    fun setUnavailable(){
        isUnavailable = true
    }

    fun setOffline() {
        isOffline = true
    }

}