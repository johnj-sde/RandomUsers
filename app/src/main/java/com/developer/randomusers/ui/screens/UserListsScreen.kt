package com.developer.randomusers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlin.math.roundToInt

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
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(
            items = users.value,
            key = {_, user ->"${user.id.name}_${user.id.value}"}
        ) { index, user ->
            UserListRow(
                user = user
            )
            if (index<users.value.size-1) {
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(modifier = Modifier, thickness = 1.dp, color = Color.Black)
            }
        }
    }
}

@Composable
fun UserListRow(
    user: User
) {

    var offsetX by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .background(Color.Red)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight().background(Color.Green).align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White
            )
        }
        UserListItem(
            user,
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                        println("offset $offsetX")
                    }
                )
        )
    }
}

@Composable
fun UserListItem(
    user: User,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .background(Color.Blue)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.25f)
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
            modifier = Modifier.weight(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

          //  DraggableText()

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

@Composable
private fun DraggableText() {
    var offsetX by remember { mutableStateOf(0f) }
    Text(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offsetX += delta
                    println("offset $offsetX")
                }
            ),
        text = "Drag me!",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
}