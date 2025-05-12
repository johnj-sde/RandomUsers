package com.developer.randomusers.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

@Composable
fun MainScreen(

) {

}



class MainViewModel(
    val restClient: RandomUserAPIClient
): ViewModel() {

    fun fetchUsers() {

        val call = restClient.fetchUsers(10)
        call.enqueue(object : Callback<ResultsAndInfo> {
            override fun onResponse(
                call: Call<ResultsAndInfo?>,
                response: Response<ResultsAndInfo?>
            ) {
                val results = response.body()?.results
                results?.let { it ->
                    for (result in it) {
                        println("result id = ${result.id}")
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