package com.jesusd0897.rickandmorty.view.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesusd0897.rickandmorty.domain.entity.CharacterEntity
import com.jesusd0897.rickandmorty.domain.entity.ErrorType
import com.jesusd0897.rickandmorty.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class HomeViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    data class Error(val type: ErrorType, val throwable: Throwable?)

    data class UiState(
        val isLoading: Boolean = false,
        val error: Error? = null,
        val characters: List<CharacterEntity> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), UiState())

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            delay(2.seconds) // Simulate network delay
            val characters = getCharactersUseCase.invoke()
            _uiState.update { it.copy(characters = characters) }
        }
    }

}