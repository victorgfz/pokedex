package com.pokedex.app.presentation.screens.pokedex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pokedex.app.presentation.components.PokemonCard
import com.pokedex.app.presentation.theme.typeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
onPokemonClick: (Int) -> Unit,
viewModel: PokedexViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchActive by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()

    val pokemonTypes = listOf("All", "Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy")

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            inputField = {
                val query = (uiState as? PokedexUiState.Success)?.searchQuery ?: ""
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = viewModel::onSearchQueryChange,
                    onSearch = { searchActive = false },
                    expanded = searchActive,
                    onExpandedChange = { searchActive = it },
                    placeholder = { Text("Buscar Pokémon...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
            },
            expanded = searchActive,
            onExpandedChange = { searchActive = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {}

        if (uiState is PokedexUiState.Success) {
            ScrollableTabRow(
                selectedTabIndex = pokemonTypes.indexOf((uiState as PokedexUiState.Success).selectedType),
                edgePadding = 16.dp,
                divider = {},
                indicator = {},
                containerColor = MaterialTheme.colorScheme.background
            ) {
                pokemonTypes.forEach { type ->
                    val isSelected = (uiState as PokedexUiState.Success).selectedType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onTypeChange(type) },
                        label = { Text(type, color = Color.White) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(
                                color = typeColor(type.lowercase()),
                                shape = RoundedCornerShape(12.dp)
                        )
                    )
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.loadPokemons()
        }

        when (val state = uiState) {
            is PokedexUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is PokedexUiState.Success -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = state.pokemons,
                        key = { _, p -> p.id }
                    ) { index, pokemon ->
                        if (index >= state.pokemons.size - 1 && !state.endReached && !state.isNextPageLoading) {
                            LaunchedEffect(Unit) {
                                viewModel.loadPokemons()
                            }
                        }

                        PokemonCard(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.id) }
                        )
                    }

                    if (state.isNextPageLoading) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }
            }

            is PokedexUiState.Error -> {
                Text("Erro desconhecido")
            }
        }
    }
}
