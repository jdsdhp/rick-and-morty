package com.jesusd0897.rickandmorty.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.ErrorType
import com.jesusd0897.rickandmorty.domain.entity.LocationEntity
import com.jesusd0897.rickandmorty.domain.entity.OriginEntity
import com.jesusd0897.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination
import com.jesusd0897.rickandmorty.view.navigation.NavKeys
import com.jesusd0897.rickandmorty.view.screen.detail.CharacterDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher(), TestRule {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

internal class CharacterDetailViewModelTest {

    @get:Rule
    internal val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase
    private lateinit var viewModel: CharacterDetailViewModel

    private val dummyCharacter = CharacterEntity(
        id = 42,
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

    @Before
    fun setup() {
        getCharacterByIdUseCase = mockk()
    }

    /**
     * Ensures ViewModel emits loading and then character success state from SavedStateHandle ID.
     */
    @Test
    fun `loads character successfully using SavedStateHandle`() = runTest {
        // Given
        val handle = SavedStateHandle(mapOf(NavKeys.ID to 42))
        coEvery { getCharacterByIdUseCase(42) } coAnswers {
            delay(1_000)
            Result.success(dummyCharacter)
        }

        // When
        viewModel = CharacterDetailViewModel(handle, getCharacterByIdUseCase)

        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertEquals(dummyCharacter, successState.character)
            assertTrue(!successState.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Ensures ViewModel emits error state when use case fails.
     */
    @Test
    fun `emits error state when use case fails`() = runTest {
        // Given
        val handle = SavedStateHandle(mapOf(NavKeys.ID to 42))
        val exception = Exception("Network failure")
        coEvery { getCharacterByIdUseCase(42) } coAnswers {
            delay(1_000)
            Result.failure(exception)
        }

        // When
        viewModel = CharacterDetailViewModel(handle, getCharacterByIdUseCase)

        // Then
        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val error = awaitItem()
            assertNotNull(error.error)
            assertEquals(ErrorType.LOAD, error.error?.type)
            assertEquals(exception, error.error?.throwable)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Ensures that navigation event is triggered on back event.
     */
    @Test
    fun `emits back nav event when onBackClick is triggered`() = runTest {
        // Given
        val handle = SavedStateHandle(mapOf(NavKeys.ID to 42))
        coEvery { getCharacterByIdUseCase(42) } coAnswers {
            delay(1_000)
            Result.success(dummyCharacter)
        }
        viewModel = CharacterDetailViewModel(handle, getCharacterByIdUseCase)

        viewModel.navEvent.test {
            viewModel.uiState.test {
                awaitItem() // loading
                awaitItem() // success
                cancelAndIgnoreRemainingEvents()
            }

            // When
            viewModel.onEvent(CharacterDetailViewModel.Event.OnBackClick)

            // Then
            assertEquals(DetailNavDestination.Back, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Ensures ViewModel emits loading state indefinitely if use case is suspended.
     */
    @Test
    fun `emits only loading state when use case is suspended`() = runTest {
        val handle = SavedStateHandle(mapOf(NavKeys.ID to 42))
        coEvery { getCharacterByIdUseCase(42) } coAnswers {
            delay(Long.MAX_VALUE)
            Result.success(dummyCharacter)
        }

        viewModel = CharacterDetailViewModel(handle, getCharacterByIdUseCase)

        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Ensures no navigation event is emitted initially.
     */
    @Test
    fun `navEvent emits nothing initially`() = runTest {
        val handle = SavedStateHandle(mapOf(NavKeys.ID to 42))
        coEvery { getCharacterByIdUseCase(42) } coAnswers {
            delay(1_000)
            Result.success(dummyCharacter)
        }

        viewModel = CharacterDetailViewModel(handle, getCharacterByIdUseCase)

        viewModel.navEvent.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}