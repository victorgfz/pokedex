package com.pokedex.app.data.remote

import com.pokedex.app.data.remote.dto.PokemonDetailDto
import com.pokedex.app.data.remote.dto.PokemonListResponseDto
import com.pokedex.app.data.remote.dto.PokemonSpeciesDto
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val BASE_URL = "https://pokeapi.co/api/v2"

class PokeApiService {

    private val client = buildHttpClient()

    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponseDto =
        client.get("$BASE_URL/pokemon?limit=$limit&offset=$offset").body()

    suspend fun getPokemonDetail(id: Int): PokemonDetailDto =
        client.get("$BASE_URL/pokemon/$id").body()

    suspend fun getPokemonSpecies(id: Int): PokemonSpeciesDto =
        client.get("$BASE_URL/pokemon-species/$id").body()
}