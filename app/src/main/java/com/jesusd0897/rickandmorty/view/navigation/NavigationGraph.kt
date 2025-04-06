package com.jesusd0897.rickandmorty.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jesusd0897.rickandmorty.view.navigation.args.CharacterArg
import com.jesusd0897.rickandmorty.view.navigation.mappers.toArg
import com.jesusd0897.rickandmorty.view.screen.detail.DetailScreen
import com.jesusd0897.rickandmorty.view.screen.home.HomeScreen
import kotlinx.serialization.json.Json
import java.net.URLEncoder

internal sealed class Nav(val route: String) {
    companion object Keys {
        const val HOME = "home"
        const val DETAIL = "detail"
        const val CHARACTER = "character"
    }

    data object Home : Nav(route = HOME)
    data object Detail : Nav(route = "$DETAIL/{$CHARACTER}") {
        fun withArg(character: CharacterArg): String = "$DETAIL/${
            URLEncoder.encode(Json.encodeToString(character), Charsets.UTF_8.name())
        }"
    }
}

@Composable
internal fun NavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Nav.Home.route
    ) {
        composable(Nav.Home.route) {
            HomeScreen(
                onNavigate = { destination ->
                    if (destination is HomeNavDestination.Detail) {
                        val characterArg: CharacterArg = destination.character.toArg()
                        navController.navigate(route = Nav.Detail.withArg(characterArg))
                    }
                }
            )
        }
        composable(
            route = Nav.Detail.route,
            arguments = listOf(navArgument(Nav.CHARACTER) {
                type = NavType.StringType
            })
        ) {
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
