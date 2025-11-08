package com.developer.randomusers.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.User
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.model.matchesName
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlin.math.roundToInt

@Composable
fun UserListScreen(
    viewModel: UserViewModel,
    navigateTo: (Int) -> Unit
) {

    val usersState by viewModel.usersState
        .collectAsStateWithLifecycle()

    val searchText by viewModel.userInputTextForSearch.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                .testTag("searchText"),
            value = searchText,
            placeholder = {
                Text(text = stringResource(R.string.search))
            },
            onValueChange = {
                    input ->
                viewModel.updateSearchText(userInput = input)
            },
        )

        UserListComposable(
            users = usersState.users,
            navigateTo = navigateTo,
            fetchUsers = viewModel::fetchUsers,
            deleteUser = viewModel::deleteUser
        )

    }

}

@Composable
fun UserListComposable(
    users: List<User>,
    navigateTo: (Int) -> Unit,
    fetchUsers: () -> Unit,
    deleteUser: (User) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val isReadyToFetch by remember {
        derivedStateOf {
            lazyListState.isCloseToEnd(offset = 3)
        }
    }

    LaunchedEffect(isReadyToFetch) {
        if (isReadyToFetch) {
            fetchUsers()
        }
    }

    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(
            items = users,
            key = {_, user ->"${user.id.name}_${user.id.value}"}
        ) { index, user ->
            UserListRow(
                user = user,
                onClickDeleteIcon = {
                    deleteUser(user)
                },
                onClickListItem = {
                    navigateTo(index)
                }
            )
            if (index < users.lastIndex) {
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(modifier = Modifier, thickness = 1.dp, color = Color.Black)
            }
        }
    }
}

@Composable
fun UserListRow(
    user: User,
    onClickDeleteIcon: () -> Unit,
    onClickListItem: () -> Unit,
) {
    var iconContainerSize by remember { mutableStateOf(IntSize.Zero) }
    val deleteIconVisibleState = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.2f)
                .align(Alignment.CenterEnd)
                .onSizeChanged{
                    iconContainerSize = it
                },
            contentAlignment = Alignment.Center
        ) {
            if (deleteIconVisibleState.value) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    modifier = Modifier.fillMaxSize(0.5f).clickable(onClick = {onClickDeleteIcon()}).testTag("deleteIcon"),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
        UserListItem(
            user,
            iconContainerSize,
            onClickListItem,
            onDeleteIconVisibilityChanged = { isVisible ->
                deleteIconVisibleState.value = isVisible
            }
        )
    }
}

@Composable
fun UserListItem(
    user: User,
    deleteIconContainerSize: IntSize,
    onClickListItem: () -> Unit,
    onDeleteIconVisibilityChanged: (Boolean) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(
            durationMillis = 200
        ),
        label = "offset animation"
    )

    val offsetLimit = -1*deleteIconContainerSize.width.toFloat()

    Row(
        modifier = Modifier
            .offset { IntOffset(animatedOffset.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    if (offsetX == 0f && delta < 0) {
                        offsetX = offsetLimit
                        onDeleteIconVisibilityChanged(true)
                    } else if (offsetX == offsetLimit && delta > 0) {
                        offsetX = 0f
                        onDeleteIconVisibilityChanged(false)
                    }
                }
            )
            .clickable(onClick = {onClickListItem()})
            .background(Color.White)
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