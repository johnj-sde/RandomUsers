package com.developer.randomusers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.developer.randomusers.ui.navigation.Routes.UserDetailScreen
import com.developer.randomusers.ui.navigation.Routes.UserListScreen
import com.developer.randomusers.ui.screens.UserDetailScreenScaffold
import com.developer.randomusers.ui.screens.UserListScreenScaffold
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel



@Composable
fun NavHostContainer(
    userViewModel: UserViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination= UserListScreen()) {
        composable<UserListScreen>(
            deepLinks = listOf(
                navDeepLink<UserListScreen>(basePath = "$DEEPLINK_SCHEME://$DEEPLINK_HOST"),
            )
        ){
            backStackEntry ->
            val userListScreen = backStackEntry.toRoute<UserListScreen>()
            userViewModel.updateSearchText(userListScreen.searchForName ?: "")
            UserListScreenScaffold(
                viewModel = userViewModel,
                navigateTo = { position ->
                    navController.navigate(UserDetailScreen(position = position))
                },
            )
        }
        composable<UserDetailScreen>(
            deepLinks = listOf(
                navDeepLink<UserDetailScreen>(basePath = "$DEEPLINK_SCHEME://$DEEPLINK_HOST/{idName}/{idValue}"),
            )
        ) { backStackEntry ->
            val userDetailScreen = backStackEntry.toRoute<UserDetailScreen>()
            UserDetailScreenScaffold(
                viewModel = userViewModel,
                position = userDetailScreen.position,
                idName = userDetailScreen.idName,
                idValue = userDetailScreen.idValue,
                handleBackPressed = {
                    navController.popBackStack()
                }
            )
        }

    }
}

@Serializable
sealed class Routes {
    @Serializable
    data class UserListScreen(val searchForName: String? = null): Routes()
    @Serializable
    data class UserDetailScreen(val position: Int = -1, val idName: String = "", val idValue: String = ""): Routes()
}

sealed class NavResult {
    object BackPressed : NavResult()
    object Idle : NavResult()
}