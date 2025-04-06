package com.jesusd0897.rickandmorty.domain.usecase

import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(private val repository: CharacterRepository) {

    private companion object {
        const val PAGE_SIZE = 20
    }

    operator fun invoke(): Flow<PagingData<CharacterEntity>> =
        repository.getCharacters(pageSize = PAGE_SIZE)

}