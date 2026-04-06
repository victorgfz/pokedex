package com.pokedex.app.presentation.screens.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pokedex.app.domain.model.PokemonDetail

@Composable
fun TeamScreen(teamViewModel: TeamViewModel) {
    val team by teamViewModel.team.collectAsState()
    PlatformTeamContent(
        pokemons = team,
        onRemove = { teamViewModel.removeFromTeam(it) }
    )
}

@Composable
expect fun PlatformTeamContent(
    pokemons: List<PokemonDetail>,
    onRemove: (pokemonId: Int) -> Unit
)