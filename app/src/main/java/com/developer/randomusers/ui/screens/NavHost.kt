package com.developer.randomusers.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.developer.randomusers.ui.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavHostContainer(
    userViewModel: UserViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination= Routes.UserListScreen) {
        composable(
            route = Routes.UserListScreen
        ){
            UserListScreen(
                viewModel = userViewModel,
                navigateTo = { position ->
                    navController.navigate(Routes.UserDetailScreen + "/$position")
                },
            )
        }
        composable(
            route = Routes.UserDetailScreen + "/{position}",
            arguments = listOf(
                navArgument(name ="position") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            UserDetailScreen(
                viewModel = userViewModel,
                position = backStackEntry.arguments?.getInt("position") ?: -1
            )
        }

    }
}

object Routes {
    const val UserListScreen = "UserListScreen"
    val UserDetailScreen = "UserDetailScreen"
}