package com.jesusd0897.rickandmorty.integration

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jesusd0897.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import com.jesusd0897.rickandmorty.fake.FakeCharacterRepository
import com.jesusd0897.rickandmorty.util.DUMMY_CHARACTER_DATA
import com.jesusd0897.rickandmorty.util.MainDispatcherRule
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination
import com.jesusd0897.rickandmorty.view.navigation.NavKeys
import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class CharacterDetailIntegrationTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // Fake repository setup
    private val repo = FakeCharacterRepository().apply { setCharacters(DUMMY_CHARACTER_DATA) }
    private val useCase = GetCharacterByIdUseCase(repo)

    /**
     * Tests that the view model successfully loads a character when the ID is valid.
     * It verifies that the UI state initially shows loading and then updates with the character data.
     */
    @Test
    fun `loads character from real use case`() = runTest {
        val characterId = 1
        val handle = SavedStateHandle(mapOf(NavKeys.ID to characterId))

        // Move test block before instantiating the viewmodel to catch emissions
        val uiStateFlow = CharacterDetailViewModel(handle, useCase).uiState

        uiStateFlow.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val success = awaitItem()
            assertEquals(DUMMY_CHARACTER_DATA.find { it.id == characterId }, success.character)
            assertFalse(success.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Tests that the view model handles the case when a character is not found.
     * It verifies that the UI state initially shows loading and then displays an error.
     */
    @Test
    fun `returns error when character not found`() = runTest {
        val characterId = 999
        val handle = SavedStateHandle(mapOf(NavKeys.ID to characterId))
        val viewModel = CharacterDetailViewModel(handle, useCase)

        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val error = awaitItem()
            assertNotNull(error.error)
            assertEquals("Character not found", error.error?.throwable?.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Tests that a navigation event is emitted when the back button is clicked.
     * It verifies that the correct destination event is emitted via the navEvent flow.
     */
    @Test
    fun `emits navigation event on back click`() = runTest {
        val characterId = 1
        val handle = SavedStateHandle(mapOf(NavKeys.ID to characterId))
        val viewModel = CharacterDetailViewModel(handle, useCase)

        viewModel.navEvent.test {
            viewModel.uiState.test {
                awaitItem() // loading
                awaitItem() // success
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.onEvent(CharacterDetailViewModel.Event.OnBackClick)
            assertEquals(DetailNavDestination.Back, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}