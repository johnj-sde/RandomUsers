package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.ui.viewmodel.UserViewModel

@Composable
fun UserListScreen(
    viewModel: UserViewModel,
    navigateTo: (Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val isReadyToFetch by remember {
        derivedStateOf {
            lazyListState.isCloseToEnd(offset = 3)
        }
    }

    LaunchedEffect(isReadyToFetch) {
        if (isReadyToFetch) {
            viewModel.fetchUsers()
        }
    }

    val users = viewModel.users.collectAsStateWithLifecycle()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            count = users.value.size,
            key = {users.value[it].id.value}
        ) { it ->
            UserListItem(
                user = users.value[it],
                modifier = Modifier.clickable(onClick = {navigateTo(it)})
            )
            if (it<users.value.size-1) {
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


private fun LazyListState.isCloseToEnd(offset: Int = 3): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 &&
            (lastVisibleItem?.index ?: -1) >= layoutInfo.totalItemsCount - offset
}