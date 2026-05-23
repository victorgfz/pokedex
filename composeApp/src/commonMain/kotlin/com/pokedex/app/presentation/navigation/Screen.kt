package com.pokedex.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Home : Screen()

    @Serializable
    data object PokedexList : Screen()

    @Serializable
    data class PokemonDetail(val pokemonId: Int) : Screen()

    @Serializable
    data object Team : Screen()
}