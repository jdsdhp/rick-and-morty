package com.jesusd0897.rickandmorty.view.navigation.args

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class OriginArg(
    val name: String,
    val url: String,
)