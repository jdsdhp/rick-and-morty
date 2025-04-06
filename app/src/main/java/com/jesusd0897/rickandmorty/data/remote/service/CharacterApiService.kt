package com.jesusd0897.rickandmorty.data.remote.service

import com.jesusd0897.rickandmorty.data.remote.dto.CharacterDto
import com.jesusd0897.rickandmorty.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CharacterApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String,
    ): CharacterResponseDto

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") characterId: Int
    ): CharacterDto
}