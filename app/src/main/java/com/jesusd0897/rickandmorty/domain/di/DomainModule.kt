package com.jesusd0897.rickandmorty.domain.di

import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetCharactersUseCase() }
}