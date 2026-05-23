package com.pokedex.app.presentation.screens.team

import androidx.lifecycle.ViewModel
import com.pokedex.app.domain.model.PokemonDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface TeamUiState {
    data class Success(val pokemons: List<PokemonDetail>) : TeamUiState
}

class TeamViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<TeamUiState>(TeamUiState.Success(emptyList()))
    val uiState: StateFlow<TeamUiState> = _uiState.asStateFlow()

    // Mantendo a propriedade 'team' para compatibilidade se necessário, mas preferindo uiState
    val team: StateFlow<List<PokemonDetail>> get() = MutableStateFlow((_uiState.value as TeamUiState.Success).pokemons).asStateFlow()

    fun addToTeam(pokemon: PokemonDetail) {
        _uiState.update { currentState ->
            if (currentState is TeamUiState.Success) {
                val currentList = currentState.pokemons
                val alreadyInTeam = currentList.any { it.id == pokemon.id }
                if (!alreadyInTeam && currentList.size < 6) {
                    TeamUiState.Success(currentList + pokemon)
                } else {
                    currentState
                }
            } else {
                currentState
            }
        }
    }

    fun removeFromTeam(pokemonId: Int) {
        _uiState.update { currentState ->
            if (currentState is TeamUiState.Success) {
                TeamUiState.Success(currentState.pokemons.filter { it.id != pokemonId })
            } else {
                currentState
            }
        }
    }

    fun isInTeam(pokemonId: Int): Boolean {
        val state = _uiState.value
        return if (state is TeamUiState.Success) {
            state.pokemons.any { it.id == pokemonId }
        } else false
    }
}
