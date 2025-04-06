package com.jesusd0897.rickandmorty.view.navigation.mappers

import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.LocationEntity
import com.jesusd0897.rickandmorty.domain.entity.OriginEntity
import com.jesusd0897.rickandmorty.view.navigation.args.CharacterArg
import com.jesusd0897.rickandmorty.view.navigation.args.LocationArg
import com.jesusd0897.rickandmorty.view.navigation.args.OriginArg

internal fun CharacterEntity.toArg() = CharacterArg(
    created = created,
    episode = episode,
    gender = gender,
    id = id,
    image = image,
    location = location.toArg(),
    name = name,
    origin = origin.toArg(),
    species = species,
    status = status,
    type = type,
    url = url,
)

internal fun LocationEntity.toArg() = LocationArg(
    name = name,
    url = url,
)

internal fun OriginEntity.toArg() = OriginArg(
    name = name,
    url = url,
)

internal fun CharacterArg.toEntity() = CharacterEntity(
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

internal fun LocationArg.toEntity() = LocationEntity(
    name = name,
    url = url,
)

internal fun OriginArg.toEntity() = OriginEntity(
    name = name,
    url = url,
)