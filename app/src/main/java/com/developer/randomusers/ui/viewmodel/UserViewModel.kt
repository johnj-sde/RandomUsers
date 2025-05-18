package com.developer.randomusers.ui.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.model.User
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class UserViewModel(
    val restClient: RandomUserAPIClient
): ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private val _state = MutableStateFlow<LazyListState>(LazyListState())
    val state = _state.asStateFlow()


    init {
        fetchUsers()
        _state.onEach { state ->
            println("lazy list state change emission")
            viewModelScope.launch {
                if (abs(users.value.size - state.firstVisibleItemIndex) <= 20) {
                    fetchUsers()
                }
            }
        }
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
                        val newList = _users.value.toMutableList() + it
                        _users.emit(newList)
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