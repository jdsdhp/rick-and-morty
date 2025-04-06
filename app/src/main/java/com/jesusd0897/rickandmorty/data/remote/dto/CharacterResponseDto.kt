package com.jesusd0897.rickandmorty.data.remote.dto

internal data class CharacterResponseDto(
    val info: PageInfoDto,
    val results: List<CharacterDto>
)

internal data class CharacterDto(
    val created: String,
    val episode: List<String?>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: LocationDto,
    val name: String,
    val origin: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)