package com.pokedex.app.presentation.screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonDetail
import com.pokedex.app.domain.repository.PokemonRepository
import com.pokedex.app.presentation.screens.pokedex.PokedexUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamMember(
    val pokemon: Pokemon,
    val capturedLocation: String
)
sealed interface TeamUiState {
    data object Loading : TeamUiState
    data class Success(val pokemons: List<TeamMember>) : TeamUiState
    data class Error(val message: String) : TeamUiState
}

class TeamViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TeamUiState>(TeamUiState.Loading)
    val uiState: StateFlow<TeamUiState> = _uiState.asStateFlow()

    init {
        loadTeam()
    }

    private fun loadTeam() {
        viewModelScope.launch {
            repository.getTeam()
                .onStart {
                    _uiState.value = TeamUiState.Loading
                }
                .catch { e ->
                    _uiState.value = TeamUiState.Error(e.message ?: "Erro ao carregar o time")
                }
                .collect { members ->
                    _uiState.value = TeamUiState.Success(members)
                }
        }
    }


    fun addToTeam(pokemon: PokemonDetail, capturedLocation: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is TeamUiState.Success) {
                val alreadyIn = currentState.pokemons.any { it.pokemon.id == pokemon.id }
                val isFull = currentState.pokemons.size >= 6

                if (alreadyIn || isFull) return@launch
            }

            try {
                repository.addToTeam(pokemon, capturedLocation)
            } catch (e: Exception) {
                _uiState.value = TeamUiState.Error("Não foi possível adicionar ao time")
            }
        }
    }

    fun removeFromTeam(pokemonId: Int) {
        viewModelScope.launch {
            try {
                repository.removeFromTeam(pokemonId)
            } catch (e: Exception) {
                _uiState.value = TeamUiState.Error("Não foi possível remover do time")
            }
        }
    }
}
