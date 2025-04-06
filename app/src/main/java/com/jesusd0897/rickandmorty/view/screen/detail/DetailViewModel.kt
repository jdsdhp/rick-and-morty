package com.jesusd0897.rickandmorty.view.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.ErrorType
import com.jesusd0897.rickandmorty.view.navigation.DetailNavDestination
import com.jesusd0897.rickandmorty.view.navigation.Nav
import com.jesusd0897.rickandmorty.view.navigation.args.CharacterArg
import com.jesusd0897.rickandmorty.view.navigation.mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import kotlin.time.Duration.Companion.seconds

internal class DetailViewModel(
    stateHandle: SavedStateHandle,
) : ViewModel() {

    data class Error(val type: ErrorType, val throwable: Throwable?)

    data class UiState(
        val isLoading: Boolean = false,
        val error: Error? = null,
        val characters: List<CharacterEntity> = emptyList()
    )

    sealed class Event {
        data object OnBackClick : Event()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), UiState())

    private val _navEvent = MutableSharedFlow<DetailNavDestination>()
    val navEvent = _navEvent.asSharedFlow()

    val character: Flow<CharacterEntity?> =
        stateHandle.getStateFlow<String?>(key = Nav.CHARACTER, initialValue = null)
            .mapNotNull { characterAsString ->
                characterAsString?.let { encodedCharacter ->
                    val decoded = URLDecoder.decode(encodedCharacter, Charsets.UTF_8.name())
                    val arg = Json.decodeFromString<CharacterArg>(decoded)
                    arg.toEntity()
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), null)

    fun onEvent(event: Event) {
        when (event) {
            Event.OnBackClick -> {
                // TODO()
            }
        }
    }

}