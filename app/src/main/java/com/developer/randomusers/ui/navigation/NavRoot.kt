package com.developer.randomusers.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.developer.randomusers.ui.screens.UserDetailScreenScaffold
import com.developer.randomusers.ui.screens.UserListScreenScaffold
import com.developer.randomusers.ui.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel



@Composable
fun NavRoot(
    userViewModel: UserViewModel = koinViewModel(),
    deeplink: Uri?,
    consumeIntent: () -> Unit
) {

    val backStack = rememberNavBackStack(UserList(""))
    val deeplinkResolver = DeeplinkResolver(UserList(""))

    LaunchedEffect(deeplink) {
        deeplink?.let {
            val destination = deeplinkResolver.resolve(it.toString())
            when (destination) {
                is UserList -> {
                    backStack.clear()
                }
                is UserDetails -> {
                    while (backStack.isNotEmpty() && (backStack.last() is UserDetails)) {
                        backStack.removeLastOrNull()
                    }
                }
            }
            backStack.add(destination)
            consumeIntent()
        }
    }
    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        entryProvider = entryProvider {
            entry<UserList> { userListNavKey ->
                userViewModel.updateSearchText(userListNavKey.searchFilter)
                UserListScreenScaffold(
                    viewModel = userViewModel,
                    navigateTo = { position ->
                        backStack.add(UserDetails(position = position))
                    },
                )
            }
            entry<UserDetails> { userDetailsNavKey ->
                UserDetailScreenScaffold(
                    viewModel = userViewModel,
                    position = userDetailsNavKey.position,
                    idName = userDetailsNavKey.idName,
                    idValue = userDetailsNavKey.idValue,
                    handleBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )

            }
        },
//        transitionSpec = {
//            slideInHorizontally(initialOffsetX = { it }) togetherWith
//                    scaleOut(targetScale = .9f)
//        },
//        popTransitionSpec = {
//            scaleIn(initialScale = .9f) togetherWith
//                    slideOutHorizontally(targetOffsetX = { it })
//        },
//        predictivePopTransitionSpec = {
//            scaleIn(initialScale = .9f) togetherWith
//                    slideOutHorizontally(targetOffsetX = { it })
//        }
    )
}