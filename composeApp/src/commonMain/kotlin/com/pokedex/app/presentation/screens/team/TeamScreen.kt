package com.pokedex.app.presentation.screens.team

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pokedex.app.domain.model.PokemonDetail

@Composable
fun TeamScreen(teamViewModel: TeamViewModel) {
    val uiState by teamViewModel.uiState.collectAsStateWithLifecycle()
    
    when (val state = uiState) {
        is TeamUiState.Success -> {
            PlatformTeamContent(
                pokemons = state.pokemons,
                onRemove = { teamViewModel.removeFromTeam(it) }
            )
        }
    }
}

@Composable
expect fun PlatformTeamContent(
    pokemons: List<TeamMember>,
    onRemove: (pokemonId: Int) -> Unit
)
