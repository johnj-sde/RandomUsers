package com.developer.randomusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(
    val restClient: RandomUserAPIClient
): ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {

        val call = restClient.fetchUsers(40)
        call.enqueue(object : Callback<ResultsAndInfo> {
            override fun onResponse(
                call: Call<ResultsAndInfo?>,
                response: Response<ResultsAndInfo?>
            ) {
                val users = response.body()?.users
                users?.let {
                    viewModelScope.launch {
                        for (result in it) {
                            println("name is ${result.name?.first?: "no name"}")
                        }
                        _users.emit(it)
                    }

                }

            }

            override fun onFailure(
                call: Call<ResultsAndInfo?>,
                t: Throwable
            ) {
                println("network fetch failed")
                println("${t.message}")
                println(t.stackTraceToString())

            }

        })

    }

}