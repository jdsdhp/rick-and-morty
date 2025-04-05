package com.jesusd0897.rickandmorty.view.navigation

internal sealed class HomeNavDestination {
    data object Detail : HomeNavDestination()
}

internal sealed class DetailNavDestination {
    data object Back : DetailNavDestination()
}