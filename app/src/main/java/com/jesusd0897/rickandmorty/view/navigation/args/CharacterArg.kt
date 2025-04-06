package com.jesusd0897.rickandmorty.view.navigation.args

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class CharacterArg(
    val created: String,
    val episode: List<String?>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: LocationArg,
    val name: String,
    val origin: OriginArg,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)