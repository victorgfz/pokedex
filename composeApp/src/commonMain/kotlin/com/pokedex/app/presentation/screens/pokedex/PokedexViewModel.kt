package com.pokedex.app.presentation.screens.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.di.AppModule
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PokedexUiState {
    data object Loading : PokedexUiState
    data class Success(
        val pokemons: List<Pokemon>,
        val searchQuery: String = "",
        val selectedType: String = "All",
        val isNextPageLoading: Boolean = false,
        val endReached: Boolean = false
    ) : PokedexUiState
    data class Error(val message: String) : PokedexUiState
}

class PokedexViewModel(
    private val getPokemonList: GetPokemonListUseCase = AppModule.getPokemonList
) : ViewModel() {
    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20
    private var searchJob: Job? = null
    private var isFetching = false

    fun loadPokemons(isNewFilter: Boolean = false) {
        if (isFetching) return

        val state = _uiState.value

        if (state is PokedexUiState.Success && state.isNextPageLoading && !isNewFilter) return
        if (state is PokedexUiState.Success && state.endReached && !isNewFilter) return

        if (isNewFilter) {
            currentOffset = 0
            _uiState.value = PokedexUiState.Loading
        }

        viewModelScope.launch {
            isFetching = true
            val query = (state as? PokedexUiState.Success)?.searchQuery ?: ""
            val type = (state as? PokedexUiState.Success)?.selectedType ?: "All"

            if (isNewFilter) {
                _uiState.value = PokedexUiState.Loading
            } else if (state is PokedexUiState.Success) {
                _uiState.update { state.copy(isNextPageLoading = true) }
            }

            runCatching {
                getPokemonList(
                    limit = pageSize,
                    offset = currentOffset,
                    searchQuery = query,
                    typeFilter = type
                )
            }.onSuccess { newList ->
                _uiState.update { currentState ->
                    val currentList = if (isNewFilter || currentState !is PokedexUiState.Success) {
                        emptyList()
                    } else {
                        currentState.pokemons
                    }

                    PokedexUiState.Success(
                        pokemons = currentList + newList,
                        searchQuery = query,
                        selectedType = type,
                        endReached = newList.size < pageSize,
                        isNextPageLoading = false
                    )
                }
                currentOffset += pageSize
            }.onFailure { e ->
                if (e is kotlinx.coroutines.CancellationException) throw e
                _uiState.value = PokedexUiState.Error(e.message ?: "Erro desconhecido")
            }
            isFetching = false
        }
    }

    fun onSearchQueryChange(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { state ->
                if (state is PokedexUiState.Success) state.copy(searchQuery = query)
                else PokedexUiState.Success(emptyList(), searchQuery = query)
            }

            delay(500)
            loadPokemons(isNewFilter = true)
        }
    }

    fun onTypeChange(type: String) {
        _uiState.update { state ->
            if (state is PokedexUiState.Success) state.copy(selectedType = type)
            else PokedexUiState.Success(emptyList(), selectedType = type)
        }
        loadPokemons(isNewFilter = true)
    }
}
