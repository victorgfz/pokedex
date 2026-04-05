package com.pokedex.app.presentation.screens.team

import androidx.lifecycle.ViewModel
import com.pokedex.app.domain.model.PokemonDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeamViewModel : ViewModel() {

    private val _team = MutableStateFlow<List<PokemonDetail>>(emptyList())
    val team: StateFlow<List<PokemonDetail>> = _team.asStateFlow()

    fun addToTeam(pokemon: PokemonDetail) {
        val current = _team.value
        val alreadyInTeam = current.any { it.id == pokemon.id }
        if (!alreadyInTeam && current.size < 6) {
            _team.value = current + pokemon
        }
    }

    fun removeFromTeam(pokemonId: Int) {
        _team.value = _team.value.filter { it.id != pokemonId }
    }

    fun isInTeam(pokemonId: Int): Boolean =
        _team.value.any { it.id == pokemonId }
}