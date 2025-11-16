package com.developer.randomusers.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.developer.randomusers.ui.screens.Routes.UserDetailScreen
import com.developer.randomusers.ui.screens.Routes.UserListScreen
import com.developer.randomusers.ui.viewmodel.UserViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavHostContainer(
    userViewModel: UserViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination= UserListScreen) {
        composable<UserListScreen>{
            UserListScreenScaffold(
                viewModel = userViewModel,
                navigateTo = { position ->
                    navController.navigate(UserDetailScreen(position = position))
                },
            )
        }
        composable<UserDetailScreen> { backStackEntry ->
            val userDetailScreen = backStackEntry.toRoute<UserDetailScreen>()
            UserDetailScreenScaffold(
                viewModel = userViewModel,
                position = userDetailScreen.position
            )
        }

    }
}

@Serializable
sealed class Routes {
    @Serializable
    data object UserListScreen: Routes()
    @Serializable
    data class UserDetailScreen(val position: Int): Routes()
}