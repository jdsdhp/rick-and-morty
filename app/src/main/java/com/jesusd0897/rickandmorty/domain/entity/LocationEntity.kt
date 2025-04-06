package com.jesusd0897.rickandmorty.domain.entity

/**
 * The location of a character.
 * @property name The name of the location.
 * @property url The URL of the location.
 */
data class LocationEntity(
    val name: String,
    val url: String
)