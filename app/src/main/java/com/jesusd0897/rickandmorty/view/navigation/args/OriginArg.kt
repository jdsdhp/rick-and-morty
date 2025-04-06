package com.jesusd0897.rickandmorty.view.navigation.args

import kotlinx.serialization.Serializable

@Serializable
internal data class OriginArg(
    val name: String,
    val url: String,
)