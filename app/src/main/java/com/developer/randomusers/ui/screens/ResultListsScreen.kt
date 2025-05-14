package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.Result
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI

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
            key = {it.id.value}
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
    Row(
        modifier = Modifier.fillMaxSize().background(Color.Green)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().weight(0.25f).background(Color.Red)
        ) {
            if (result.picture?.medium != null) {
                val uri = URI(result.picture.medium)
              /*  AsyncImage(
                    model = uri,
                    contentDescription = null
                )*/
                Image(
                    painter = painterResource(R.drawable.anonymous_avatar),
                    contentDescription = null,
                    modifier = Modifier.size(93.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.anonymous_avatar),
                    contentDescription = null,
                    modifier = Modifier.size(93.dp)
                )
            }

        }
       /* Spacer(
            modifier = Modifier.fillMaxHeight().width(5.dp)
        )*/

        Column(
            modifier = Modifier.fillMaxSize().weight(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val title = result.name?.title ?: ""
            val fullName = result.name?.first.let { firstName ->
                if (firstName==null) {
                    "Anonymous"
                } else {
                    result.name?.last.let { lastName ->
                        if (lastName==null) {
                            firstName
                        } else {
                            "$firstName $lastName"
                        }
                    }
                }
            }

            val titledName = "$title $fullName"

            Text(text = titledName)

            if (result.email != null) {
                Text(result.email)
            }

            if (result.phone != null) {
                Text(result.phone)
            }
        }
    }
}




class MainViewModel(
    val restClient: RandomUserAPIClient
): ViewModel() {

    private val _resultsAndInfo = MutableStateFlow<List<Result>>(emptyList())
    val results = _resultsAndInfo.asStateFlow()

    fun fetchUsers() {

        val call = restClient.fetchUsers(10)
        call.enqueue(object : Callback<ResultsAndInfo> {
            override fun onResponse(
                call: Call<ResultsAndInfo?>,
                response: Response<ResultsAndInfo?>
            ) {
                val results = response.body()?.results
                results?.let {
                    viewModelScope.launch {
                        for (result in it) {
                            println("name is ${result.name?.first?: "no name"}")
                        }
                        _resultsAndInfo.emit(it)
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