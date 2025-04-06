package com.jesusd0897.rickandmorty.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailScreen
import com.jesusd0897.rickandmorty.view.screen.home.HomeScreen

/**
 * Navigation destinations
 * @param route The route to navigate to.
 */
internal sealed class Nav(val route: String) {
    companion object Keys {
        const val HOME = "home"
        const val CHARACTER_DETAIL = "character"
        const val CHARACTER_ID = "characterId"
    }

    data object Home : Nav(route = HOME)
    data object Detail : Nav(route = "$CHARACTER_DETAIL/{$CHARACTER_ID}") {
        fun withArg(characterId: Int): String = "$CHARACTER_DETAIL/${characterId}"
    }
}

/**
 * Navigation graph for the app.
 * @param navController The navigation controller.
 */
@Composable
internal fun NavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Nav.Home.route
    ) {
        composable(Nav.Home.route) {
            HomeScreen(
                onNavigate = { destination ->
                    if (destination is HomeNavDestination.CharacterDetail) {
                        navController.navigate(route = Nav.Detail.withArg(destination.characterId))
                    }
                }
            )
        }
        composable(
            route = Nav.Detail.route,
            arguments = listOf(navArgument(Nav.CHARACTER_ID) { type = NavType.IntType }),
        ) {
            CharacterDetailScreen(
                onNavigate = { destination ->
                    if (destination is DetailNavDestination.Back) {
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}
