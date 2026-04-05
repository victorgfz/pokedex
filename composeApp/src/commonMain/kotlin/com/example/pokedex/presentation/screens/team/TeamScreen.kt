package com.pokedex.app.presentation.screens.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pokedex.app.domain.model.PokemonDetail

// Wrapper compartilhado — coleta o estado e delega para a implementação de plataforma
@Composable
fun TeamScreen(teamViewModel: TeamViewModel) {
    val team by teamViewModel.team.collectAsState()
    PlatformTeamContent(
        pokemons = team,
        onRemove = { teamViewModel.removeFromTeam(it) }
    )
}

// expect — cada plataforma fornece sua própria implementação visual
@Composable
expect fun PlatformTeamContent(
    pokemons: List<PokemonDetail>,
    onRemove: (pokemonId: Int) -> Unit
)