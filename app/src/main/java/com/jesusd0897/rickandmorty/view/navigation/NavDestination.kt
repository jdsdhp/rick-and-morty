package com.jesusd0897.rickandmorty.view.navigation

/**
 * Navigation destinations for the home screen.
 */
internal sealed class HomeNavDestination {

    /**
     * Navigates to the character detail screen.
     * @param characterId The ID of the character.
     */
    data class CharacterDetail(val characterId: Int) : HomeNavDestination()
}

/**
 * Navigation destinations for the detail screen.
 */
internal sealed class DetailNavDestination {

    /**
     * Navigates back to the home screen.
     */
    data object Back : DetailNavDestination()
}