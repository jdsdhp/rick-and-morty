package com.jesusd0897.rickandmorty.fake

import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.seconds

internal class FakeCharacterRepository : CharacterRepository {

    private var characters = listOf<CharacterEntity>()

    fun setCharacters(data: List<CharacterEntity>) {
        characters = data
    }

    override fun getCharacters(
        pageSize: Int,
        nameQuery: String
    ): Flow<PagingData<CharacterEntity>> {

        val filtered = if (nameQuery.isNotBlank()) {
            characters.filter { it.name.contains(nameQuery, ignoreCase = true) }
        } else characters

        return flowOf(PagingData.from(filtered)).onStart { 3.seconds }
    }

    override suspend fun getCharacterById(characterId: Int): Result<CharacterEntity> {
        val character = characters.find { it.id == characterId }
        delay(3.seconds) // Simulate network delay
        return if (character != null) {
            Result.success(character)
        } else {
            Result.failure(Exception("Character not found"))
        }
    }
}