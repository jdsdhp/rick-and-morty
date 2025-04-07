package com.jesusd0897.rickandmorty.view.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.ErrorType
import com.jesusd0897.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination
import com.jesusd0897.rickandmorty.view.navigation.NavKeys
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * View model for the character detail screen.
 * @property savedStateHandle The saved state handle.
 * @property getCharacterByIdUseCase The use case to get a character by id.
 */
internal class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
) : ViewModel() {

    /**
     * The UI state.
     * @property isLoading Whether the UI is loading.
     * @property error The error that occurred.
     */
    data class Error(val type: ErrorType, val throwable: Throwable?)

    /**
     * The UI state.
     * @property isLoading Whether the UI is loading.
     * @property error The error that occurred.
     * @property character The character that was loaded.
     */
    data class UiState(
        val isLoading: Boolean = false,
        val error: Error? = null,
        val character: CharacterEntity? = null,
    )

    /**
     * The events that can be triggered.
     */
    sealed class Event {
        data object OnBackClick : Event()
    }

    private val _uiState = MutableStateFlow(UiState())

    /**
     * The UI state.
     */
    val uiState = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), UiState())

    private val _navEvent = MutableSharedFlow<DetailNavDestination>()

    /**
     * The navigation events.
     */
    val navEvent = _navEvent.asSharedFlow()

    /**
     * The character ID using navigation argument.
     */
    private val characterId: Int by lazy { savedStateHandle.get<Int>(NavKeys.ID)!! }

    init {
        fetchCharacterById()
    }

    /**
     * Handles events.
     * @param event The event to handle.
     */
    fun onEvent(event: Event) {
        if (event == Event.OnBackClick) onBackClick()
    }

    /**
     * Handles the back click.
     */
    private fun onBackClick() {
        viewModelScope.launch {
            _navEvent.emit(DetailNavDestination.Back)
        }
    }

    /**
     * Fetches a character by id.
     */
    private fun fetchCharacterById() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getCharacterByIdUseCase(id = characterId)
                .onSuccess { character ->
                    _uiState.update { it.copy(character = character, isLoading = false) }
                }.onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            error = Error(ErrorType.LOAD, throwable),
                            isLoading = false,
                        )
                    }
                }
        }

    }

}