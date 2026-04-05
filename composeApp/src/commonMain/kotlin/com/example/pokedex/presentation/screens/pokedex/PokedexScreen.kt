package com.pokedex.app.presentation.screens.pokedex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pokedex.app.presentation.components.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: PokedexViewModel = viewModel { PokedexViewModel() }
) {
    val uiState   by viewModel.uiState.collectAsStateWithLifecycle()
    val query     by viewModel.searchQuery.collectAsStateWithLifecycle()
    var searchActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        // SearchBar do Material3
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query          = query,
                    onQueryChange  = viewModel::onSearchQueryChange,
                    onSearch       = { searchActive = false },
                    expanded       = searchActive,
                    onExpandedChange = { searchActive = it },
                    placeholder    = { Text("Buscar Pokémon...") },
                    leadingIcon    = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                )
            },
            expanded       = searchActive,
            onExpandedChange = { searchActive = it },
            modifier       = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {}

        when (val state = uiState) {
            is PokedexUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(16.dp))
                        Text("Carregando Pokémons...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            is PokedexUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ops! Algo deu errado.", fontWeight = FontWeight.Bold)
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.onSearchQueryChange("") }) { Text("Tentar novamente") }
                    }
                }
            }

            is PokedexUiState.Success -> {
                LazyVerticalGrid(
                    columns             = GridCells.Fixed(2),
                    contentPadding      = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier            = Modifier.fillMaxSize()
                ) {
                    items(state.pokemons, key = { it.id }) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.id) }
                        )
                    }
                }
            }
        }
    }
}