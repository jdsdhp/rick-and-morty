package com.jesusd0897.rickandmorty.view.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import com.jesusd0897.rickandmorty.view.navigation.HomeNavDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * View model for the home screen.
 * @property getCharactersUseCase The use case to get characters.
 */
internal class HomeViewModel(
    getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    /**
     * The UI state.
     */
    data class UiState(
        val searchQuery: String = "",
    )

    /**
     * The events that can be triggered.
     */
    sealed class Event {
        data object OnRefresh : Event()
        data class OnSearchQueryChange(val query: String) : Event()
        data class OnItemClick(val character: CharacterEntity) : Event()
    }

    private val _uiState = MutableStateFlow(UiState())

    /**
     * The UI state.
     */
    val uiState = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), UiState())

    private val _navEvent = MutableSharedFlow<HomeNavDestination>()

    /**
     * The navigation events.
     */
    val navEvent = _navEvent.asSharedFlow()

    /**
     * The trigger to refresh the data.
     */
    private val refreshTrigger = MutableStateFlow(0)

    /**
     * The search job.
     */
    private var searchJob: Job? = null

    /**
     * The characters. Use a trigger to refresh the data.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val characters: Flow<PagingData<CharacterEntity>> = refreshTrigger
        .flatMapLatest {
            getCharactersUseCase(nameQuery = _uiState.value.searchQuery)
        }
        .cachedIn(viewModelScope)

    /**
     * Handles events.
     * @param event The event to handle.
     */
    fun onEvent(event: Event) {
        when (event) {
            is Event.OnRefresh -> refreshData()
            is Event.OnItemClick -> onCharacterClick(event.character)
            is Event.OnSearchQueryChange -> onSearchQueryChange(event.query)
        }
    }

    /**
     * Handles the search query change.
     * @param query The new search query.
     */
    private fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(1.5.seconds) // User input delay to avoid too many requests
            refreshData()
        }
    }

    /**
     * Handles the character click.
     * @param character The character that was clicked.
     */
    private fun onCharacterClick(character: CharacterEntity) {
        viewModelScope.launch {
            _navEvent.emit(HomeNavDestination.CharacterDetail(characterId = character.id))
        }
    }

    /**
     * Refreshes the data.
     */
    private fun refreshData() {
        refreshTrigger.update { it.inc() }
    }

}