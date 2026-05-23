package com.pokedex.app.presentation.theme

import androidx.compose.ui.graphics.Color

val typeColors: Map<String, Color> = mapOf(
    "fire"     to Color(0xFFFF6B35),
    "water"    to Color(0xFF4FC3F7),
    "grass"    to Color(0xFF66BB6A),
    "electric" to Color(0xFFFFD54F),
    "psychic"  to Color(0xFFEC407A),
    "ice"      to Color(0xFF80DEEA),
    "dragon"   to Color(0xFF7E57C2),
    "dark"     to Color(0xFF546E7A),
    "fairy"    to Color(0xFFF48FB1),
    "normal"   to Color(0xFFBDBDBD),
    "fighting" to Color(0xFFEF5350),
    "flying"   to Color(0xFF90CAF9),
    "poison"   to Color(0xFFAB47BC),
    "ground"   to Color(0xFFD4A843),
    "rock"     to Color(0xFF8D6E63),
    "bug"      to Color(0xFF9CCC65),
    "ghost"    to Color(0xFF7986CB),
    "steel"    to Color(0xFF90A4AE),
    "unknown"  to Color(0xFF78909C),
    "shadow"   to Color(0xFF37474F)
)

fun typeColor(type: String): Color = typeColors[type] ?: Color(0xFFBDBDBD)