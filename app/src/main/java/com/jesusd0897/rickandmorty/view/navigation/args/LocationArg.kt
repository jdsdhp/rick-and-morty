package com.jesusd0897.rickandmorty.view.navigation.args

import kotlinx.serialization.Serializable

@Serializable
internal data class LocationArg(
    val name: String,
    val url: String,
)