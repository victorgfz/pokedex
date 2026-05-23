package com.pokedex.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.presentation.theme.typeColor

@Composable
fun PokemonCard(pokemon: Pokemon, onClick: () -> Unit) {
    val primaryColor = typeColor(pokemon.types.firstOrNull() ?: "normal")

    ElevatedCard(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().aspectRatio(0.82f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            primaryColor.copy(alpha = 0.80f),
                            primaryColor.copy(alpha = 0.35f)
                        )
                    )
                )
        ) {
            Text(
                text     = "#${pokemon.id.toString().padStart(3, '0')}",
                fontSize = 11.sp,
                color    = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.align(Alignment.TopStart).padding(10.dp)
            )

            AsyncImage(
                model          = pokemon.imageUrl,
                contentDescription = pokemon.name,
                contentScale   = ContentScale.Fit,
                modifier       = Modifier
                    .size(90.dp)
                    .align(Alignment.Center)
                    .padding(top = 12.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text       = pokemon.name,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    pokemon.types.forEach { TypeBadge(it) }
                }
            }
        }
    }
}