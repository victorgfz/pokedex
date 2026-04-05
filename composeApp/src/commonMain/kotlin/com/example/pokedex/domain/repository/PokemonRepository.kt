package com.pokedex.app.domain.repository

import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonDetail

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>
    suspend fun getPokemonDetail(id: Int): PokemonDetail
}