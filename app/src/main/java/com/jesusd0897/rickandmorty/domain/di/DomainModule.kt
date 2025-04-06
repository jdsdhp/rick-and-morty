package com.jesusd0897.rickandmorty.domain.di

import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import org.koin.dsl.module

/**
 * Koin module for domain layer.
 */
val domainModule = module {
    // Use cases
    single { GetCharactersUseCase(get()) }
}