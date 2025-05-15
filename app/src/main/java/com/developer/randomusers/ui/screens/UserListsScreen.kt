package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
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
    users: List<User>,
    navigateTo: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            count = users.size,
            key = {users[it].id.value}
        ) {
            UserListItem(
                user = users[it],
                modifier = Modifier.clickable(onClick = {navigateTo(it)})
            )
            if (it<users.size-1) {
                Spacer(modifier = Modifier.fillMaxWidth().height(5.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)
            }
        }
    }
}


@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max).fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.25f)
        ) {
            if (user.picture?.medium != null) {
                AsyncImage(
                    model = user.picture.medium,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.anonymous_avatar),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }


        }
       /* Spacer(
            modifier = Modifier.fillMaxHeight().width(5.dp)
        )*/

        Column(
            modifier = Modifier.fillMaxWidth().weight(0.75f),
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

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {

        val call = restClient.fetchUsers(20)
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