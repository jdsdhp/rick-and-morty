package com.jesusd0897.rickandmorty.domain.entity

/**
 * The character entity for the app.
 * @property created The date the character was created.
 * @property episode The list of episodes the character appeared in.
 * @property gender The gender of the character.
 * @property id The ID of the character.
 * @property image The URL of the character's image.
 * @property location The location the character is from.
 * @property name The name of the character.
 * @property origin The origin of the character.
 * @property species The species of the character.
 * @property status The status of the character.
 * @property type The type of the character.
 * @property url The URL of the character.
 */
data class CharacterEntity(
    val created: String,
    val episode: List<String?>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: LocationEntity,
    val name: String,
    val origin: OriginEntity,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)