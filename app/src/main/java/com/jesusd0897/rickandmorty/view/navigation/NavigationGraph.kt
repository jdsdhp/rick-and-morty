package com.jesusd0897.rickandmorty.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jesusd0897.rickandmorty.view.screen.detail.DetailScreen
import com.jesusd0897.rickandmorty.view.screen.home.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Detail : Screen("detail")
}

@Composable
internal fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigate = { destination ->
                    if (destination is HomeNavDestination.Detail) {
                        navController.navigate(Screen.Detail.route)
                    }
                }
            )
        }
        composable(Screen.Detail.route) {
            DetailScreen(
                onNavigate = { destination ->
                    if (destination is DetailNavDestination.Back) {
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}
