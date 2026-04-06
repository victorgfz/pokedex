package com.pokedex.app.presentation.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateToPokedex: () -> Unit,
    onNavigateToTeam: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
                )
            )
    ) {
        PokeballDecoration(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(x = 90.dp, y = (-90).dp)
                .alpha(0.10f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text       = "Pokédex",
                fontSize   = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White,
                lineHeight = 56.sp
            )

            Text(
                text     = "Explore todos os 151 Pokémons",
                fontSize = 16.sp,
                color    = Color.White.copy(alpha = 0.65f),
                modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
            )

            HomeActionCard(
                title    = "Pokédex",
                subtitle = "Veja todos os Pokémons",
                icon     = Icons.Filled.GridView,
                gradient = listOf(Color(0xFFE63946), Color(0xFFFF6B6B)),
                onClick  = onNavigateToPokedex
            )

            Spacer(Modifier.height(16.dp))

            HomeActionCard(
                title    = "Meu Time",
                subtitle = "Monte sua equipe",
                icon     = Icons.Filled.Star,
                gradient = listOf(Color(0xFF457B9D), Color(0xFF1D3557)),
                onClick  = onNavigateToTeam
            )
        }
    }
}

@Composable
private fun HomeActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(88.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradient))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Column {
                    Text(text = title,    fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = subtitle, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
private fun PokeballDecoration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r  = size.minDimension / 2f

        drawArc(
            color      = Color.Red,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter  = true,
            size       = Size(r * 2, r * 2),
            topLeft    = Offset(cx - r, cy - r)
        )
        drawArc(
            color      = Color.White,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter  = true,
            size       = Size(r * 2, r * 2),
            topLeft    = Offset(cx - r, cy - r)
        )
        drawLine(
            color       = Color.Black,
            start       = Offset(cx - r, cy),
            end         = Offset(cx + r, cy),
            strokeWidth = r * 0.06f,
            cap         = StrokeCap.Round
        )
        drawCircle(
            color  = Color.Black,
            radius = r * 0.18f,
            style  = Stroke(width = r * 0.06f)
        )
        drawCircle(color = Color.White, radius = r * 0.12f)
    }
}