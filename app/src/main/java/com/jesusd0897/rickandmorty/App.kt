package com.jesusd0897.rickandmorty

import android.app.Application
import com.jesusd0897.rickandmorty.data.di.dataModule
import com.jesusd0897.rickandmorty.domain.di.domainModule
import com.jesusd0897.rickandmorty.view.di.viewModule
import org.koin.core.context.GlobalContext.startKoin

/**
 * Application class
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize dependency injection
        startKoin {
            modules(domainModule, dataModule, viewModule)
        }
    }

}