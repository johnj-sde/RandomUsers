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
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.developer.randomusers.R
import com.developer.randomusers.model.Id
import com.developer.randomusers.model.User
import com.developer.randomusers.model.UsersState
import com.developer.randomusers.model.getFullName
import com.developer.randomusers.ui.navigation.NavResult
import com.developer.randomusers.ui.screens.previewutils.previewUser1
import com.developer.randomusers.ui.screens.previewutils.previewUserState
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlin.math.roundToInt

@Composable
fun UserListScreenScaffold(
    viewModel: UserViewModel,
    navigateTo: (Int) -> Unit
){
    val usersState by viewModel.usersState
        .collectAsStateWithLifecycle()
    val searchText by viewModel.userInputTextForSearch.collectAsStateWithLifecycle()

    val navResult by viewModel.result.collectAsStateWithLifecycle()
    val mqttMessageState by viewModel.mqttMessageFlow.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        UserListScreen(
            usersState = usersState,
            searchText = searchText,
            navResult = navResult,
            navigateTo = navigateTo,
            onSearchInput = viewModel::updateSearchText,
            fetchUsers = viewModel::fetchUsers,
            deleteUser = viewModel::deleteUser,
            consumeResult = viewModel::consumeNavigationResult,
            toggleFavorite = viewModel::toggleFavorite,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun UserListScreen(
    usersState: UsersState,
    searchText: String,
    navResult: NavResult,
    navigateTo: (Int) -> Unit,
    onSearchInput: (String) -> Unit,
    fetchUsers: () -> Unit,
    deleteUser: (User) -> Unit,
    consumeResult: () -> Unit,
    toggleFavorite: (Id) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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
                    input -> onSearchInput(input)
            },
        )

        UserListComposable(
            users = usersState.users,
            searchText = searchText,
            navResult = navResult,
            navigateTo = navigateTo,
            fetchUsers = fetchUsers,
            deleteUser = deleteUser,
            toggleFavorite = toggleFavorite,
            consumeResult = consumeResult
        )

    }

}

@Composable
private fun UserListComposable(
    users: List<User>,
    searchText: String,
    navResult: NavResult,
    navigateTo: (Int) -> Unit,
    fetchUsers: () -> Unit,
    deleteUser: (User) -> Unit,
    consumeResult: () -> Unit,
    toggleFavorite: (Id) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val isReadyToFetch by remember {
        derivedStateOf {
            lazyListState.isCloseToEnd(offset = 3)
        }
    }

    LaunchedEffect(users) {
        if (searchText.isNotEmpty() && navResult is NavResult.Idle) {
            lazyListState.scrollToItem(0)
        } else if (navResult is NavResult.BackPressed) {
            consumeResult()
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
                },
                toggleFavorite = toggleFavorite
            )
            if (index < users.lastIndex) {
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalDivider(modifier = Modifier, thickness = 1.dp, color = Color.Black)
            }
        }
    }
}

@Composable
private fun UserListRow(
    user: User,
    onClickDeleteIcon: () -> Unit,
    onClickListItem: () -> Unit,
    toggleFavorite: (Id) -> Unit
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
                .onSizeChanged {
                    iconContainerSize = it
                },
            contentAlignment = Alignment.Center
        ) {
            if (deleteIconVisibleState.value) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .clickable(onClick = { onClickDeleteIcon() })
                        .testTag("deleteIcon"),
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
            },
            toggleFavorite = toggleFavorite
        )
    }
}

@Composable
fun UserListItem(
    user: User,
    deleteIconContainerSize: IntSize,
    onClickListItem: () -> Unit,
    onDeleteIconVisibilityChanged: (Boolean) -> Unit,
    toggleFavorite: (Id) -> Unit
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
            .clickable(onClick = { onClickListItem() })
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
        Row(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.8f),
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

            Column(
                modifier = Modifier.weight(0.2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.clickable(onClick = {toggleFavorite(user.id)}),
                    contentAlignment = Alignment.Center
                ) {
                    if (user.isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "A Favorite User",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = "Not a Favorite User",
                            tint = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

            }
        }


    }
}


private fun LazyListState.isCloseToEnd(offset: Int = 3): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 &&
            (lastVisibleItem?.index ?: -1) >= layoutInfo.totalItemsCount - offset
}


@Preview(showBackground = true)
@Composable
private fun UserListScreenPreview() {
    UserListScreen(
        usersState = previewUserState,
        searchText = "preview",
        navResult = NavResult.Idle,
        navigateTo = { },
        onSearchInput = { },
        fetchUsers = { },
        deleteUser = { },
        consumeResult = { },
        toggleFavorite = { },
        paddingValues = PaddingValues()
    )
}

@Preview(showBackground = true)
@Composable
private fun UserListsComposablePreview() {
    UserListComposable(
        users = previewUserState.users,
        searchText = "",
        navigateTo = { },
        fetchUsers = { },
        deleteUser = { },
        navResult = NavResult.Idle,
        consumeResult = { },
        toggleFavorite = {  }
    )
}

@Preview(showBackground = true)
@Composable
private fun UserListItemPreview(){
    UserListItem(
        user = previewUser1,
        deleteIconContainerSize = IntSize.Zero,
        onClickListItem = { } ,
        onDeleteIconVisibilityChanged = { },
        toggleFavorite = { }
    )
}