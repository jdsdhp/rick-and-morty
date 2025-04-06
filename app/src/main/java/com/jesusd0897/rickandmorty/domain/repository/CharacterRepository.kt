package com.jesusd0897.rickandmorty.domain.repository

import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacters(pageSize: Int): Flow<PagingData<CharacterEntity>>

}