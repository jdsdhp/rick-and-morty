package com.jesusd0897.rickandmorty.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailScreen
import com.jesusd0897.rickandmorty.view.screen.home.HomeScreen
import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the app.
 */
object NavKeys {
    const val ID = "id"
}

@Serializable
private object Home

@Serializable
private data class CharacterDetail(val id: Int)

/**
 * Navigation graph for the app.
 * @param navController The navigation controller.
 */
@Composable
internal fun NavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> {
            HomeScreen(
                onNavigate = { destination ->
                    if (destination is HomeNavDestination.CharacterDetail) {
                        navController.navigate(CharacterDetail(id = destination.characterId))
                    }
                }
            )
        }
        composable<CharacterDetail> {
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
