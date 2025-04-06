package com.jesusd0897.rickandmorty.view.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.ErrorType
import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import com.jesusd0897.rickandmorty.view.navigation.HomeNavDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

internal class HomeViewModel(
    getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    data class Error(val type: ErrorType, val throwable: Throwable?)

    data class UiState(
        val error: Error? = null,
    )

    sealed class Event {
        data object OnRefresh : Event()
        data object OnSearchClick : Event()
        data class OnItemClick(val character: CharacterEntity) : Event()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), UiState())

    private val _navEvent = MutableSharedFlow<HomeNavDestination>()
    val navEvent = _navEvent.asSharedFlow()

    private val refreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val characters: Flow<PagingData<CharacterEntity>> = refreshTrigger
        .flatMapLatest {
            getCharactersUseCase()
        }
        .cachedIn(viewModelScope)

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnRefresh -> refreshData()
            is Event.OnSearchClick -> onSearchClick()
            is Event.OnItemClick -> onCharacterClick(event.character)
        }
    }

    private fun onCharacterClick(character: CharacterEntity) {
        viewModelScope.launch {
            _navEvent.emit(HomeNavDestination.Detail(character = character))
        }
    }

    private fun onSearchClick() {
        // TODO("Not yet implemented")
    }

    private fun refreshData() {
        refreshTrigger.update { it.inc() }
    }

}