package com.jesusd0897.rickandmorty.view.navigation

import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity

/**
 * Navigation destinations for the home screen.
 */
internal sealed class HomeNavDestination {
    data class Detail(val character: CharacterEntity) : HomeNavDestination()
}

/**
 * Navigation destinations for the detail screen.
 */
internal sealed class DetailNavDestination {
    data object Back : DetailNavDestination()
}