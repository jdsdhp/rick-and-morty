package com.jesusd0897.rickandmorty.view.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.ErrorType
import com.jesusd0897.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination
import com.jesusd0897.rickandmorty.view.navigation.Nav
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

internal class CharacterDetailViewModel(
    stateHandle: SavedStateHandle,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
) : ViewModel() {

    data class Error(val type: ErrorType, val throwable: Throwable?)

    data class UiState(
        val isLoading: Boolean = false,
        val error: Error? = null,
        val character: CharacterEntity? = null,
    )

    sealed class Event {
        data object OnBackClick : Event()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), UiState())

    private val _navEvent = MutableSharedFlow<DetailNavDestination>()
    val navEvent = _navEvent.asSharedFlow()

    private val characterId: Int by lazy { stateHandle.get<Int>(Nav.CHARACTER_ID)!! }

    init {
        fetchCharacterById()
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.OnBackClick -> onBackClick()
        }
    }

    private fun onBackClick() {
        viewModelScope.launch {
            _navEvent.emit(DetailNavDestination.Back)
        }
    }

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