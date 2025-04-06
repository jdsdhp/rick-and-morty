package com.jesusd0897.rickandmorty.view.di

import com.jesusd0897.rickandmorty.view.screen.detail.DetailViewModel
import com.jesusd0897.rickandmorty.view.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for view models.
 */
val viewModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}