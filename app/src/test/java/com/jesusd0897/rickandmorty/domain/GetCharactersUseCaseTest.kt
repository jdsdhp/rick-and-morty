package com.jesusd0897.rickandmorty.domain

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.repository.CharacterRepository
import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import com.jesusd0897.rickandmorty.util.CharacterDiffCallback
import com.jesusd0897.rickandmorty.util.DUMMY_CHARACTER_DATA
import com.jesusd0897.rickandmorty.util.NoopListCallback
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetCharactersUseCaseTest {

    private lateinit var repository: CharacterRepository
    private lateinit var useCase: GetCharactersUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCharactersUseCase(repository)
    }

    /**
     * Verifies that the use case correctly filters characters by name
     * and emits a PagingData containing only the matching entries.
     * The test simulates UI behavior using AsyncPagingDataDiffer.
     */
    @Test
    fun `returns filtered PagingData when repository emits matching characters`() {
        val scheduler = TestCoroutineScheduler()
        val dispatcher = StandardTestDispatcher(scheduler)

        runTest(scheduler) {
            // Filter dummy characters by name query
            val nameQuery = "Rick"
            val characters = DUMMY_CHARACTER_DATA.filter {
                it.name.contains(nameQuery, ignoreCase = true)
            }

            // Create PagingData from the filtered list
            val pagingData: PagingData<CharacterEntity> = PagingData.from(characters)

            // Mock repository response
            every {
                repository.getCharacters(pageSize = 20, nameQuery = nameQuery)
            } returns flowOf(pagingData)

            // Invoke use case inside the test dispatcher
            val result = withContext(dispatcher) {
                useCase.invoke(nameQuery).first()
            }

            // Create an AsyncPagingDataDiffer to simulate UI behavior
            val differ = AsyncPagingDataDiffer(
                diffCallback = CharacterDiffCallback(),
                updateCallback = NoopListCallback(),
                mainDispatcher = dispatcher,
                workerDispatcher = dispatcher
            )

            // Submit the data and allow processing
            differ.submitData(result)
            advanceUntilIdle()

            // Assert that the result matches expected size and first item
            assertEquals(characters.size, differ.snapshot().size)
            assertEquals(characters.first().name, differ.snapshot()[0]?.name)
        }
    }

}