package com.pokedex.app.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val stats: List<PokemonStat>,
    val description: String,
    val heightM: Float,
    val weightKg: Float
)

data class PokemonStat(
    val name: String,
    val value: Int
)