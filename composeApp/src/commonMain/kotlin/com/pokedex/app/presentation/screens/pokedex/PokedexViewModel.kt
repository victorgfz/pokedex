package com.pokedex.app.presentation.screens.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.di.AppModule
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.usecase.GetPokemonListUseCase
import com.pokedex.app.domain.usecase.SearchPokemonUseCase
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
        val isNextPageLoading: Boolean = false,
        val endReached: Boolean = false
    ) : PokedexUiState
    data class Error(val message: String) : PokedexUiState
}

class PokedexViewModel(
    private val getPokemonList: GetPokemonListUseCase = AppModule.getPokemonList,
    private val searchPokemon: SearchPokemonUseCase = AppModule.searchPokemon
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private val pageSize = 20
    private var searchJob: Job? = null

    init {
        loadPokemons()
    }

    fun loadPokemons() {
        if (_uiState.value is PokedexUiState.Success && (_uiState.value as PokedexUiState.Success).isNextPageLoading) return

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is PokedexUiState.Success) {
                _uiState.update { currentState.copy(isNextPageLoading = true) }
            } else {
                _uiState.value = PokedexUiState.Loading
            }

            runCatching { 
                getPokemonList(limit = pageSize, offset = currentPage * pageSize) 
            }.onSuccess { newList ->
                _uiState.update { state ->
                    val currentList = if (state is PokedexUiState.Success) state.pokemons else emptyList()
                    val updatedList = currentList + newList
                    PokedexUiState.Success(
                        pokemons = updatedList,
                        endReached = newList.size < pageSize,
                        isNextPageLoading = false
                    )
                }
                currentPage++
            }.onFailure { e ->
                if (_uiState.value is PokedexUiState.Loading) {
                    _uiState.value = PokedexUiState.Error(
                        e.message ?: "Erro ao carregar Pokémons."
                    )
                } else {
                    _uiState.update { (it as PokedexUiState.Success).copy(isNextPageLoading = false) }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchJob?.cancel()

        _uiState.update { state ->
            if (state is PokedexUiState.Success) state.copy(searchQuery = query) else state
        }

        if (query.isBlank()) {
            resetPagination()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            runCatching {
                searchPokemon(query)
            }.onSuccess { results ->
                _uiState.update { state ->
                    if (state is PokedexUiState.Success) {
                        state.copy(
                            pokemons = results,
                            endReached = true // Disable pagination during search
                        )
                    } else {
                        PokedexUiState.Success(pokemons = results, searchQuery = query, endReached = true)
                    }
                }
            }
        }
    }

    private fun resetPagination() {
        currentPage = 0
        _uiState.value = PokedexUiState.Loading
        loadPokemons()
    }
}
