package com.jesusd0897.rickandmorty.integration

/**
 * Integration tests for [HomeViewModel], testing its interaction with [GetCharactersUseCase] and [FakeCharacterRepository].
 * These tests validate real interactions between ViewModel, UseCase, and Repository layers using in-memory data.
 */

import androidx.paging.PagingData
import app.cash.turbine.test
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import com.jesusd0897.rickandmorty.fake.FakeCharacterRepository
import com.jesusd0897.rickandmorty.util.DUMMY_CHARACTER_DATA
import com.jesusd0897.rickandmorty.util.MainDispatcherRule
import com.jesusd0897.rickandmorty.util.collectData
import com.jesusd0897.rickandmorty.view.navigation.HomeNavDestination
import com.jesusd0897.rickandmorty.view.screen.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class HomeIntegrationTest {

    @get:Rule
    internal val mainDispatcherRule = MainDispatcherRule()

    // Fake repository setup
    private val repo = FakeCharacterRepository().apply { setCharacters(DUMMY_CHARACTER_DATA) }
    private val useCase = GetCharactersUseCase(repo)
    private val viewModel = HomeViewModel(useCase)

    /**
     * Verifies that the ViewModel emits correctly filtered character results based on the search query.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `ViewModel emits filtered characters via UseCase and FakeRepository`() = runTest {
        val query = "Summer"

        // Emit search event
        viewModel.onEvent(HomeViewModel.Event.OnSearchQueryChange(query = query))

        advanceUntilIdle() // Let the delay pass

        viewModel.characters.test {
            val result: PagingData<CharacterEntity> = awaitItem()
            val items = mutableListOf<CharacterEntity>()
            result.collectData().forEach { items.add(it) }

            assertEquals(1, items.size)
            assert(items.all { it.name.contains(query) })

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that the ViewModel updates emitted data when the search query changes multiple times.
     */
    @Test
    fun `ViewModel updates results when searchQuery changes consecutively`() = runTest {
        viewModel.onEvent(HomeViewModel.Event.OnSearchQueryChange("Summer"))

        viewModel.characters.test {
            val result: List<CharacterEntity> = awaitItem().collectData() // First emission
            assertEquals(1, result.size)

            viewModel.onEvent(HomeViewModel.Event.OnSearchQueryChange("Rick"))

            val result2: List<CharacterEntity> = awaitItem().collectData() // Second emission
            assertEquals(1, result2.size)

            val summer = result.first().name
            val rick = result2.first().name

            assert(summer != rick)
        }
    }

    /**
     * Verifies that a navigation event is emitted when a character is clicked.
     */
    @Test
    fun `ViewModel emits navEvent on item click`() = runTest {
        val repo = FakeCharacterRepository().apply { setCharacters(DUMMY_CHARACTER_DATA) }
        val useCase = GetCharactersUseCase(repo)
        val viewModel = HomeViewModel(useCase)
        val character = DUMMY_CHARACTER_DATA.first()

        viewModel.navEvent.test {
            viewModel.onEvent(HomeViewModel.Event.OnItemClick(character))
            assertEquals(
                HomeNavDestination.CharacterDetail(character.id),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}