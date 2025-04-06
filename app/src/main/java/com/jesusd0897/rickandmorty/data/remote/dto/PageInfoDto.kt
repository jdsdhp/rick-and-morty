package com.jesusd0897.rickandmorty.data.remote.dto

internal data class PageInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)