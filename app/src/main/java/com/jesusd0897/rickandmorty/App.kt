package com.jesusd0897.rickandmorty

import android.app.Application
import com.jesusd0897.rickandmorty.domain.di.domainModule
import com.jesusd0897.rickandmorty.view.di.viewModule
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(domainModule, viewModule)
        }
    }
}