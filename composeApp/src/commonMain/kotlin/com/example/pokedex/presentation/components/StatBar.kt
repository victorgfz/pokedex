package com.pokedex.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

private val statDisplayNames = mapOf(
    "hp"              to "HP",
    "attack"          to "Ataque",
    "defense"         to "Defesa",
    "special-attack"  to "Sp. Atq",
    "special-defense" to "Sp. Def",
    "speed"           to "Velocidade"
)

private fun statColor(value: Int): Color = when {
    value < 50  -> Color(0xFFEF5350)
    value < 80  -> Color(0xFFFF9800)
    value < 110 -> Color(0xFF66BB6A)
    else        -> Color(0xFF42A5F5)
}

@Composable
fun StatBar(name: String, value: Int) {
    var animate by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue    = if (animate) value / 255f else 0f,
        animationSpec  = tween(durationMillis = 800),
        label          = "stat_progress"
    )

    LaunchedEffect(Unit) { animate = true }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text      = statDisplayNames[name] ?: name,
            fontSize  = 12.sp,
            fontWeight = FontWeight.Medium,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier  = Modifier.width(88.dp)
        )
        Text(
            text      = value.toString(),
            fontSize  = 12.sp,
            fontWeight = FontWeight.Bold,
            color     = statColor(value),
            modifier  = Modifier.width(36.dp)
        )
        LinearProgressIndicator(
            progress     = { progress },
            color        = statColor(value),
            trackColor   = MaterialTheme.colorScheme.surfaceVariant,
            modifier     = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}