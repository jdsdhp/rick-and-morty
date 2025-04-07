package com.jesusd0897.rickandmorty.domain

import com.jesusd0897.rickandmorty.DUMMY_CHARACTER_DATA
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import com.jesusd0897.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCharacterByIdUseCaseTest {

    private lateinit var repository: CharacterRepository
    private lateinit var useCase: GetCharacterByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCharacterByIdUseCase(repository)
    }

    @Test
    fun `returns character when repository returns success`() = runTest {
        val characterId = 1
        val character = DUMMY_CHARACTER_DATA.first { it.id == characterId }

        coEvery { repository.getCharacterById(characterId) } returns Result.success(character)

        val result = useCase(characterId)

        assertTrue(result.isSuccess)
        assertEquals(character, result.getOrNull())
    }

    @Test
    fun `returns failure when repository throws error`() = runTest {
        val characterId = 999
        val exception = Exception("Character not found")

        coEvery { repository.getCharacterById(characterId) } returns Result.failure(exception)

        val result = useCase(characterId)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}