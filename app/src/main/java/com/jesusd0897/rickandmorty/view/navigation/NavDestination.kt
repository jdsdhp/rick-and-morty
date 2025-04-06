package com.jesusd0897.rickandmorty.view.navigation

import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity

internal sealed class HomeNavDestination {
    data class Detail(val character: CharacterEntity) : HomeNavDestination()
}

internal sealed class DetailNavDestination {
    data object Back : DetailNavDestination()
}