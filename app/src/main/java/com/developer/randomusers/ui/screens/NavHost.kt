package com.developer.randomusers.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.developer.randomusers.model.User


@Composable
fun NavHostContainer(
    list: List<User>
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination= Routes.UserListScreen) {
        composable(
            route = Routes.UserListScreen
        ){
            UserListScreen(
                users = list,
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
                users = list,
                position = backStackEntry.arguments?.getInt("position") ?: -1
            )
        }

    }
}

object Routes {
    const val UserListScreen = "UserListScreen"
    val UserDetailScreen = "UserDetailScreen"
}