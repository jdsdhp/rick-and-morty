package com.jesusd0897.rickandmorty.view.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class SplashViewModel : ViewModel() {

    var keepSplashScreen = true
        private set

    init {
        viewModelScope.launch {
            delay(2.seconds)
            keepSplashScreen = false
        }
    }

}