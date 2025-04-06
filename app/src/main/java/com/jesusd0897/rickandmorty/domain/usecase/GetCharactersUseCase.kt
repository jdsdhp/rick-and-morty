package com.jesusd0897.rickandmorty.domain.usecase

import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting characters.
 * @property repository The character repository.
 */
class GetCharactersUseCase(private val repository: CharacterRepository) {

    private companion object {
        const val PAGE_SIZE = 20 // The number of items per page
    }

    /**
     * Fetches characters from the repository and returns a flow of paging data.
     * @return A flow of paging data of characters.
     */
    operator fun invoke(): Flow<PagingData<CharacterEntity>> =
        repository.getCharacters(pageSize = PAGE_SIZE)

}