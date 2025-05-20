package com.developer.randomusers.network

import com.developer.randomusers.model.ResultsAndInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://randomuser.me/api/"


interface RandomUserAPIClient {


    @GET("/api")
    suspend fun fetchUsers(@Query("results") limit: Int): ResultsAndInfo


}