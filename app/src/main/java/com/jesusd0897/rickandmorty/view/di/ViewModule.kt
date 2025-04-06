package com.jesusd0897.rickandmorty.view.di

import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailViewModel
import com.jesusd0897.rickandmorty.view.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for view models.
 */
val viewModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::CharacterDetailViewModel)
}