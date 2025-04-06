package com.jesusd0897.rickandmorty.data.di

import com.jesusd0897.rickandmorty.data.remote.service.CharacterApiService
import com.jesusd0897.rickandmorty.data.repository.CharacterRepositoryImpl
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Koin module for data layer.
 */
private val repositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
}

/**
 * The base URL for the API.
 */
private const val BASE_URL = "https://rickandmortyapi.com/api/"

/**
 * Koin module for network layer.
 */
private val networkModule = module {
    single {
        OkHttpClient.Builder().build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    single<CharacterApiService> {
        get<Retrofit>().create(CharacterApiService::class.java)
    }

}

/**
 * Koin module for data layer.
 */
val dataModule = module {
    includes(networkModule, repositoryModule)
}