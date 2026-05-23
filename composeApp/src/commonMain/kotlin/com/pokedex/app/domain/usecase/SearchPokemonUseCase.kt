package com.pokedex.app.domain.usecase

import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.repository.PokemonRepository

class SearchPokemonUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(query: String): List<Pokemon> =
        repository.searchPokemons(query)
}