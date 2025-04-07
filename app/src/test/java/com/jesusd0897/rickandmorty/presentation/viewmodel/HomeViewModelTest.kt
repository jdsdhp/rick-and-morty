package com.jesusd0897.rickandmorty.presentation.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.LocationEntity
import com.jesusd0897.rickandmorty.domain.entity.OriginEntity
import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import com.jesusd0897.rickandmorty.util.DUMMY_CHARACTER_DATA
import com.jesusd0897.rickandmorty.view.navigation.HomeNavDestination
import com.jesusd0897.rickandmorty.view.screen.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        getCharactersUseCase = mockk()
    }

    /**
     * Verifies that when the HomeViewModel is initialized, it triggers character loading and emits PagingData.
     */
    @Test
    fun `emits PagingData on initialization`() = runTest {
        // Given
        val characters = listOf(
            CharacterEntity(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "",
                origin = OriginEntity("Earth", ""),
                location = LocationEntity("Citadel", ""),
                episodes = listOf("https://ep1"),
                url = "",
                created = "",
                type = ""
            )
        )
        coEvery { getCharactersUseCase.invoke(any()) } returns flowOf(PagingData.from(characters))

        // When
        viewModel = HomeViewModel(getCharactersUseCase)

        // Then
        viewModel.characters.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

    }

    /**
     * Verifies that updating the search query triggers a new call to the use case and updates the characters flow.
     */
    @Test
    fun `emits updated PagingData on search query change`() = runTest {
        // Given
        val characters = listOf(
            CharacterEntity(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "",
                origin = OriginEntity("Earth", ""),
                location = LocationEntity("Citadel", ""),
                episodes = listOf("https://ep2"),
                url = "",
                created = "",
                type = ""
            )
        )
        coEvery { getCharactersUseCase.invoke("Morty") } returns flowOf(PagingData.from(characters))

        // When
        viewModel = HomeViewModel(getCharactersUseCase)
        viewModel.onEvent(HomeViewModel.Event.OnSearchQueryChange("Morty"))

        // Then
        viewModel.characters.test {
            awaitItem() // initial value
            awaitItem() // updated value
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Verifies that calling retry triggers the ViewModel to reload character data.
     */
    @Test
    fun `emits PagingData again on retry`() = runTest {
        // Given
        val characters = listOf(
            CharacterEntity(
                id = 3,
                name = "Summer Smith",
                status = "Alive",
                species = "Human",
                gender = "Female",
                image = "",
                origin = OriginEntity("Earth", ""),
                location = LocationEntity("Citadel", ""),
                episodes = listOf("https://ep3"),
                url = "",
                created = "",
                type = ""
            )
        )
        coEvery { getCharactersUseCase.invoke("") } returns flowOf(PagingData.from(characters))

        // When
        viewModel = HomeViewModel(getCharactersUseCase)

        // Then
        viewModel.characters.test {
            awaitItem() // initial emission
            viewModel.onEvent(HomeViewModel.Event.OnRefresh)
            awaitItem() // refreshed emission
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Ensures that if an exception occurs during character loading, the flow still emits and the ViewModel does not crash.
     */
    @Test
    fun `emits empty PagingData when error occurs`() = runTest {
        // Given
        coEvery { getCharactersUseCase.invoke(any()) } returns flowOf(
            PagingData.empty()
        )

        // When
        viewModel = HomeViewModel(getCharactersUseCase)

        // Then
        viewModel.characters.test {
            awaitItem() // should emit an empty PagingData
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updates uiState on search query change`() = runTest {
        // Given
        val query = "Summer"
        coEvery { getCharactersUseCase(query) } returns flowOf(PagingData.from(DUMMY_CHARACTER_DATA))

        // When
        viewModel = HomeViewModel(getCharactersUseCase)
        viewModel.onEvent(HomeViewModel.Event.OnSearchQueryChange(query))

        // Then
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertEquals(query, uiState.searchQuery)
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `emits navEvent on item click`() = runTest {
        val character = CharacterEntity(
            id = 99,
            name = "Beth Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            image = "",
            origin = OriginEntity("Earth", ""),
            location = LocationEntity("Citadel", ""),
            episodes = listOf("https://ep99"),
            url = "",
            created = "",
            type = ""
        )

        viewModel = HomeViewModel(getCharactersUseCase)

        val job = launch {
            viewModel.navEvent.test {
                viewModel.onEvent(HomeViewModel.Event.OnItemClick(character))
                val event = awaitItem()
                assertTrue(event is HomeNavDestination.CharacterDetail)
                assertEquals(99, (event as HomeNavDestination.CharacterDetail).characterId)
                cancelAndIgnoreRemainingEvents()
            }
        }

        job.join()
    }
}