package com.jesusd0897.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jesusd0897.rickandmorty.view.navigation.NavigationGraph
import com.jesusd0897.rickandmorty.view.screen.splash.SplashViewModel
import com.jesusd0897.rickandmorty.view.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add splash screen
        installSplashScreen().setKeepOnScreenCondition { splashViewModel.keepSplashScreen }

        // Enable edge to edge to support all Android S+ devices
        enableEdgeToEdge()

        // Set Compose content
        setContent {
            RickAndMortyTheme {
                NavigationGraph()
            }
        }
    }
}