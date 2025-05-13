package com.developer.randomusers.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.developer.randomusers.model.Result
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.network.RandomUserAPIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ResultListScreen(
    results: List<Result>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = results,
            key = {it.id}
        ) {
            ResultListItem(
                result = it
            )
        }
    }
}


@Composable
fun ResultListItem(
    result: Result,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

    }
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