package com.jesusd0897.rickandmorty.data.remote.service

import com.jesusd0897.rickandmorty.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CharacterApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDto
}