package com.jesusd0897.rickandmorty.domain.repository

import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for characters.
 */
interface CharacterRepository {

    /**
     * Fetches characters from the repository and returns a flow of paging data.
     * @param pageSize The number of items per page.
     * @param nameQuery The name query.
     * @return A flow of paging data of characters.
     */
    fun getCharacters(pageSize: Int, nameQuery: String): Flow<PagingData<CharacterEntity>>

}