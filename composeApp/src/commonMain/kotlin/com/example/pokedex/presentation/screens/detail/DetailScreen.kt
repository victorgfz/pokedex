package com.pokedex.app.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.pokedex.app.domain.model.PokemonDetail
import com.pokedex.app.presentation.components.StatBar
import com.pokedex.app.presentation.components.TypeBadge
import com.pokedex.app.presentation.screens.team.TeamViewModel
import com.pokedex.app.presentation.theme.typeColor

@Composable
fun DetailScreen(
    pokemonId: Int,
    teamViewModel: TeamViewModel,
    onBack: () -> Unit,
    detailViewModel: DetailViewModel = viewModel { DetailViewModel() }
) {
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val team    by teamViewModel.team.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) { detailViewModel.load(pokemonId) }

    when (val state = uiState) {
        is DetailUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailUiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }

        is DetailUiState.Success -> {
            val pokemon    = state.pokemon
            val inTeam     = team.any { it.id == pokemon.id }
            val teamFull   = team.size >= 6 && !inTeam
            val headerColor = typeColor(pokemon.types.firstOrNull() ?: "normal")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // ── Cabeçalho com gradiente ───────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    headerColor.copy(alpha = 0.95f),
                                    headerColor.copy(alpha = 0.50f),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    AsyncImage(
                        model              = pokemon.imageUrl,
                        contentDescription = pokemon.name,
                        contentScale       = ContentScale.Fit,
                        modifier           = Modifier
                            .size(200.dp)
                            .align(Alignment.Center)
                    )
                }

                // ── Conteúdo principal ────────────────────────────────────────
                Surface(
                    shape    = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    modifier = Modifier.fillMaxWidth().offset(y = (-20).dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        // Nome e número
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(
                                text       = pokemon.name,
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text     = "#${pokemon.id.toString().padStart(3, '0')}",
                                fontSize = 20.sp,
                                color    = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Tipos
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier              = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        ) {
                            pokemon.types.forEach { TypeBadge(it) }
                        }

                        // Medidas
                        MeasureRow(pokemon)

                        Divider(modifier = Modifier.padding(vertical = 16.dp))

                        // Descrição
                        Text("Sobre", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text     = pokemon.description,
                            fontSize = 14.sp,
                            color    = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )

                        // Stats
                        Text("Status Base", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        pokemon.stats.forEach { stat ->
                            StatBar(name = stat.name, value = stat.value)
                        }

                        Spacer(Modifier.height(24.dp))

                        // Botão adicionar ao time
                        Button(
                            onClick  = {
                                if (inTeam) teamViewModel.removeFromTeam(pokemon.id)
                                else        teamViewModel.addToTeam(pokemon)
                            },
                            enabled  = !teamFull,
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = if (inTeam) MaterialTheme.colorScheme.error
                                else        headerColor
                            ),
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) {
                            Text(
                                text       = when {
                                    inTeam    -> "Remover do Time"
                                    teamFull  -> "Time Completo (6/6)"
                                    else      -> "Adicionar ao Time"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MeasureRow(pokemon: PokemonDetail) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MeasureItem(label = "Altura", value = "${"%.1f".format(pokemon.heightM)} m")
        VerticalDivider(modifier = Modifier.height(40.dp))
        MeasureItem(label = "Peso",   value = "${"%.1f".format(pokemon.weightKg)} kg")
    }
}

@Composable
private fun MeasureItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}