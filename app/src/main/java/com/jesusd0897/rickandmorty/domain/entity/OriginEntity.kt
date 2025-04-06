package com.jesusd0897.rickandmorty.domain.entity

/**
 * The origin of a character.
 * @property name The name of the origin.
 * @property url The URL of the origin.
 */
data class OriginEntity(
    val name: String,
    val url: String
)