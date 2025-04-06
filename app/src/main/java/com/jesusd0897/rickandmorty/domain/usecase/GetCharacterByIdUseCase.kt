package com.jesusd0897.rickandmorty.domain.usecase

import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository

/**
 * Use case for getting a character by its ID.
 * @property characterRepository The character repository.
 */
class GetCharacterByIdUseCase(private val characterRepository: CharacterRepository) {

    /**
     * Fetches a character by its ID from the repository.
     * @param id The ID of the character.
     * @return A result containing the character entity or an error.
     */
    suspend operator fun invoke(id: Int): Result<CharacterEntity> =
        characterRepository.getCharacterById(id)

}