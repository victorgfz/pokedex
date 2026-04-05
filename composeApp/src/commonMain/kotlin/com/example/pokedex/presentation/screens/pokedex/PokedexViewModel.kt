package com.pokedex.app.presentation.screens.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.di.AppModule
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokedexUiState {
    data object Loading : PokedexUiState()
    data class  Success(val pokemons: List<Pokemon>) : PokedexUiState()
    data class  Error(val message: String) : PokedexUiState()
}

class PokedexViewModel(
    private val getPokemonList: GetPokemonListUseCase = AppModule.getPokemonList
) : ViewModel() {

    private val allPokemons = mutableListOf<Pokemon>()

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init { loadPokemons() }

    private fun loadPokemons() {
        viewModelScope.launch {
            _uiState.value = PokedexUiState.Loading
            runCatching { getPokemonList() }
                .onSuccess { list ->
                    allPokemons.clear()
                    allPokemons.addAll(list)
                    _uiState.value = PokedexUiState.Success(list)
                }
                .onFailure { e ->
                    _uiState.value = PokedexUiState.Error(e.message ?: "Erro desconhecido")
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        val filtered = if (query.isBlank()) allPokemons.toList()
        else allPokemons.filter {
            it.name.contains(query, ignoreCase = true) || it.id.toString() == query
        }
        if (_uiState.value !is PokedexUiState.Loading) {
            _uiState.value = PokedexUiState.Success(filtered)
        }
    }
}