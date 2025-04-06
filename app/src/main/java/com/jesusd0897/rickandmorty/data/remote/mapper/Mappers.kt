package com.jesusd0897.rickandmorty.data.remote.mapper

import com.jesusd0897.rickandmorty.data.remote.dto.CharacterDto
import com.jesusd0897.rickandmorty.data.remote.dto.LocationDto
import com.jesusd0897.rickandmorty.data.remote.dto.OriginDto
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.LocationEntity
import com.jesusd0897.rickandmorty.domain.entity.OriginEntity

internal fun CharacterDto.toEntity() = CharacterEntity(
    created = created,
    episode = episode,
    gender = gender,
    id = id,
    image = image,
    location = location.toEntity(),
    name = name,
    origin = origin.toEntity(),
    species = species,
    status = status,
    type = type,
    url = url,
)

internal fun LocationDto.toEntity() = LocationEntity(
    name = name,
    url = url,
)

internal fun OriginDto.toEntity() = OriginEntity(
    name = name,
    url = url,
)