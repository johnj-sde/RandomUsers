package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import com.developer.randomusers.R
import com.developer.randomusers.model.User
import com.developer.randomusers.model.ResultsAndInfo
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.network.RandomUserAPIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI

@Composable
fun UserListScreen(
    users: List<User>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = users,
            key = {it.id.value}
        ) {
            UserListItem(
                user = it
            )
        }
    }
}


@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxSize().background(Color.Green)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().weight(0.25f).background(Color.Red)
        ) {
            if (user.picture?.medium != null) {
                val uri = URI(user.picture.medium)
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
            
            Text(text = user.getFullName())

            if (user.email != null) {
                Text(user.email)
            }

            if (user.phone != null) {
                Text(user.phone)
            }
        }
    }
}




class MainViewModel(
    val restClient: RandomUserAPIClient
): ViewModel() {

    private val _resultsAndInfo = MutableStateFlow<List<User>>(emptyList())
    val results = _resultsAndInfo.asStateFlow()

    fun fetchUsers() {

        val call = restClient.fetchUsers(10)
        call.enqueue(object : Callback<ResultsAndInfo> {
            override fun onResponse(
                call: Call<ResultsAndInfo?>,
                response: Response<ResultsAndInfo?>
            ) {
                val results = response.body()?.users
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